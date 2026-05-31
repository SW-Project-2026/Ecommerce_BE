package com.web.ecommerce.domain.order.service;

import com.web.ecommerce.domain.address.entity.Address;
import com.web.ecommerce.domain.address.exception.AddressErrorCode;
import com.web.ecommerce.domain.address.repository.AddressRepository;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.enums.DiscountType;
import com.web.ecommerce.domain.coupon.exception.CouponErrorCode;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.order.entity.Order;
import com.web.ecommerce.domain.order.entity.OrderDetail;
import com.web.ecommerce.domain.order.enums.OrderStatus;
import com.web.ecommerce.domain.order.exception.OrderErrorCode;
import com.web.ecommerce.domain.order.mapper.OrderMapper;
import com.web.ecommerce.domain.order.repository.OrderRepository;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.exception.ProductErrorCode;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId, Long orderId) {
        Order order = getOrderOwnedByUser(userId, orderId);
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new CustomException(AddressErrorCode.ADDRESS_NOT_FOUND));
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException(AddressErrorCode.ADDRESS_ACCESS_DENIED);
        }

        List<OrderDetail> details = new ArrayList<>();
        int totalAmount = 0;

        for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new CustomException(OrderErrorCode.INSUFFICIENT_STOCK);
            }

            int unitPrice = product.getMinPrice();
            totalAmount += unitPrice * item.getQuantity();

            details.add(OrderDetail.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(unitPrice)
                    .build());

            product.update(product.getName(), product.getDescription(),
                    product.getMinPrice(), product.getMaxPrice(),
                    product.getStockQuantity() - item.getQuantity(),
                    product.getProductCategory(), product.getIsActive(), null);
        }

        int discountAmount = 0;
        UserCoupon userCoupon = null;

        if (request.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findById(request.getUserCouponId())
                    .orElseThrow(() -> new CustomException(CouponErrorCode.USER_COUPON_NOT_FOUND));
            if (!userCoupon.getUser().getId().equals(userId)) {
                throw new CustomException(CouponErrorCode.USER_COUPON_NOT_FOUND);
            }

            discountAmount = calculateDiscount(userCoupon, totalAmount);
            userCoupon.use();
        }

        Order order = Order.builder()
                .user(user)
                .address(address)
                .userCoupon(userCoupon)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalAmount(totalAmount - discountAmount)
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        for (OrderDetail detail : details) {
            OrderDetail savedDetail = OrderDetail.builder()
                    .order(order)
                    .product(detail.getProduct())
                    .quantity(detail.getQuantity())
                    .unitPrice(detail.getUnitPrice())
                    .build();
            order.getOrderDetails().add(savedDetail);
        }

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = getOrderOwnedByUser(userId, orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new CustomException(OrderErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.getOrderDetails().forEach(detail ->
                detail.getProduct().update(
                        detail.getProduct().getName(),
                        detail.getProduct().getDescription(),
                        detail.getProduct().getMinPrice(),
                        detail.getProduct().getMaxPrice(),
                        detail.getProduct().getStockQuantity() + detail.getQuantity(),
                        detail.getProduct().getProductCategory(),
                        detail.getProduct().getIsActive(),
                        null
                )
        );

        if (order.getUserCoupon() != null) {
            // 쿠폰 복구는 정책에 따라 결정 — PG 연동 시 환불과 함께 처리
        }

        order.cancel();
    }

    private int calculateDiscount(UserCoupon userCoupon, int totalAmount) {
        var coupon = userCoupon.getCoupon();

        if (coupon.getMinOrderAmount() != null && totalAmount < coupon.getMinOrderAmount()) {
            throw new CustomException(OrderErrorCode.COUPON_MIN_ORDER_NOT_MET);
        }

        if (coupon.getDiscountType() == DiscountType.FIXED) {
            return coupon.getDiscountAmount();
        } else {
            int discount = totalAmount * coupon.getDiscountAmount() / 100;
            if (coupon.getMaxDiscountAmount() != null) {
                discount = Math.min(discount, coupon.getMaxDiscountAmount());
            }
            return discount;
        }
    }

    private Order getOrderOwnedByUser(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException(OrderErrorCode.ORDER_ACCESS_DENIED);
        }
        return order;
    }
}
