package com.web.ecommerce.domain.home.service;

import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.repository.CouponRepository;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.home.dto.HomeResponse;
import com.web.ecommerce.domain.home.dto.HomeResponse.BestProductItem;
import com.web.ecommerce.domain.home.dto.HomeResponse.CouponItem;
import com.web.ecommerce.domain.home.dto.HomeResponse.ProductItem;
import com.web.ecommerce.domain.home.repository.HomeEventLogRepository;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.domain.wishlist.repository.WishlistRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private static final int INTEREST_CATEGORY_LIMIT = 5;
    private static final int PRODUCT_LIMIT = 10;
    private static final int BEST_LIMIT_LOGGED_IN = 3;
    private static final int PROMOTION_LIMIT = 4;
    private static final int RECENT_DAYS = 30;

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final HomeEventLogRepository homeEventLogRepository;

    public HomeResponse getHomeForGuest() {
        List<BestProductItem> best = getBestProducts(BEST_LIMIT_LOGGED_IN, Collections.emptySet());
        List<ProductItem> recommended = getFallbackProducts(Collections.emptySet());

        List<Product> randomPool = productRepository.findRandomActiveProducts(PRODUCT_LIMIT * 2);
        int mid = Math.min(PRODUCT_LIMIT, randomPool.size());
        List<ProductItem> recentViewed = toProductItems(randomPool.subList(0, mid), Collections.emptySet());
        List<ProductItem> purchased = toProductItems(randomPool.subList(mid, randomPool.size()), Collections.emptySet());

        List<CouponItem> promotions = getPromotions(null);
        return new HomeResponse(null, recommended, recentViewed, purchased, best, promotions);
    }

    public HomeResponse getHomeForUser(Long userId) {
        User user = userRepository.findByIdAndIsActive(userId, 1).orElse(null);
        String userName = user != null ? user.getName() : null;

        Set<Long> wishedProductIds = wishlistRepository.findProductIdsByUserId(userId);

        List<ProductItem> recommended = getRecommendedProducts(userId, wishedProductIds);
        List<ProductItem> recentViewed = getRecentViewedProducts(userId, wishedProductIds);
        List<ProductItem> purchased = getPurchasedProducts(userId, wishedProductIds);
        List<BestProductItem> best = getBestProducts(BEST_LIMIT_LOGGED_IN, wishedProductIds);
        List<CouponItem> promotions = getPromotions(userId);

        return new HomeResponse(userName, recommended, recentViewed, purchased, best, promotions);
    }

    private List<ProductItem> getRecommendedProducts(Long userId, Set<Long> wishedProductIds) {
        List<String> categories = homeEventLogRepository.findInterestCategories(userId, RECENT_DAYS, INTEREST_CATEGORY_LIMIT);
        if (categories.isEmpty()) {
            return toProductItems(productRepository.findRandomActiveProducts(PRODUCT_LIMIT), wishedProductIds);
        }
        List<Product> products = productRepository.findByInterestCategoriesAndIsActive(
                categories, PageRequest.of(0, PRODUCT_LIMIT));
        return toProductItems(products, wishedProductIds);
    }

    private List<ProductItem> getRecentViewedProducts(Long userId, Set<Long> wishedProductIds) {
        List<Long> productIds = homeEventLogRepository.findRecentViewedProductIds(userId, RECENT_DAYS, PRODUCT_LIMIT);
        if (productIds.isEmpty()) {
            return toProductItems(productRepository.findRandomActiveProducts(PRODUCT_LIMIT), wishedProductIds);
        }
        List<Product> products = productRepository.findByProductIdInAndIsActive(productIds);
        // Preserve event_log order
        List<Product> ordered = new ArrayList<>(productIds.size());
        for (Long id : productIds) {
            products.stream().filter(p -> p.getProductId().equals(id)).findFirst().ifPresent(ordered::add);
        }
        return toProductItems(ordered, wishedProductIds);
    }

    private List<ProductItem> getPurchasedProducts(Long userId, Set<Long> wishedProductIds) {
        List<Product> products = productRepository.findPurchasedProductsByUserId(userId, PRODUCT_LIMIT);
        if (products.isEmpty()) {
            return toProductItems(productRepository.findRandomActiveProducts(PRODUCT_LIMIT), wishedProductIds);
        }
        return toProductItems(products, wishedProductIds);
    }

    private List<ProductItem> getFallbackProducts(Set<Long> wishedProductIds) {
        List<Product> allBest = productRepository.findBestSellingProducts(RECENT_DAYS, BEST_LIMIT_LOGGED_IN + PRODUCT_LIMIT);
        List<Product> afterTop3 = allBest.size() > BEST_LIMIT_LOGGED_IN
                ? allBest.subList(BEST_LIMIT_LOGGED_IN, allBest.size())
                : List.of();
        if (afterTop3.size() < PRODUCT_LIMIT) {
            return toProductItems(productRepository.findRandomActiveProducts(PRODUCT_LIMIT), wishedProductIds);
        }
        return toProductItems(afterTop3, wishedProductIds);
    }

    private List<BestProductItem> getBestProducts(int limit, Set<Long> wishedProductIds) {
        List<Product> products = productRepository.findBestSellingProducts(RECENT_DAYS, limit);
        if (products.isEmpty()) {
            products = productRepository.findRandomActiveProducts(limit);
        }
        return toBestProductItems(products, wishedProductIds);
    }

    private List<CouponItem> getPromotions(Long userId) {
        List<Coupon> coupons = couponRepository.findRecent(PageRequest.of(0, PROMOTION_LIMIT));
        LocalDateTime now = LocalDateTime.now();

        return coupons.stream()
                .filter(c -> c.getExpiredAt() == null
                        || (c.getCreatedAt() != null && c.getCreatedAt().plusDays(c.getExpiredAt()).isAfter(now)))
                .map(c -> {
                    boolean hasReceived = userId != null && userCouponRepository.existsByUserIdAndCouponId(userId, c.getId());
                    String expiredAt = null;
                    if (c.getExpiredAt() != null && c.getCreatedAt() != null) {
                        LocalDate expiryDate = c.getCreatedAt().plusDays(c.getExpiredAt()).toLocalDate();
                        expiredAt = expiryDate.toString();
                    }
                    return new CouponItem(c.getId(), c.getName(), c.getDiscountAmount() != null ? c.getDiscountAmount() : 0, c.getDiscountType() != null ? c.getDiscountType().name() : null, expiredAt, hasReceived);
                })
                .toList();
    }

    private List<ProductItem> toProductItems(List<Product> products, Set<Long> wishedProductIds) {
        return products.stream()
                .map(p -> new ProductItem(
                        p.getProductId(),
                        p.getName(),
                        p.getMinPrice(),
                        p.getProductCategory() != null ? p.getProductCategory() : "",
                        p.getImageUrl() != null ? p.getImageUrl() : "",
                        wishedProductIds.contains(p.getProductId())
                ))
                .toList();
    }

    private List<BestProductItem> toBestProductItems(List<Product> products, Set<Long> wishedProductIds) {
        List<BestProductItem> result = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            result.add(new BestProductItem(
                    i + 1,
                    p.getProductId(),
                    p.getName(),
                    p.getMinPrice(),
                    p.getProductCategory() != null ? p.getProductCategory() : "",
                    p.getImageUrl() != null ? p.getImageUrl() : "",
                    wishedProductIds.contains(p.getProductId())
            ));
        }
        return result;
    }
}
