package com.web.ecommerce.domain.home.service;

import com.web.ecommerce.domain.ad.entity.AD;
import com.web.ecommerce.domain.ad.repository.AdRepository;
import com.web.ecommerce.domain.campaign.repository.CampaignTargetRepository;
import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.repository.CouponRepository;
import com.web.ecommerce.domain.coupon.repository.UserCouponRepository;
import com.web.ecommerce.domain.home.dto.HomeResponse;
import com.web.ecommerce.domain.home.dto.HomeResponse.AdItem;
import com.web.ecommerce.domain.home.dto.HomeResponse.BestProductItem;
import com.web.ecommerce.domain.home.dto.HomeResponse.CouponItem;
import com.web.ecommerce.domain.home.dto.HomeResponse.ProductItem;
import com.web.ecommerce.domain.home.repository.HomeEventLogRepository;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.wishlist.repository.WishlistRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private static final int INTEREST_CATEGORY_LIMIT = 5;
    private static final int PRODUCT_LIMIT = 12;
    private static final int BEST_LIMIT_LOGGED_IN = 3;
    private static final int PROMOTION_LIMIT = 4;
    private static final int RECENT_DAYS = 30;
    private static final int SECTION_MIN_THRESHOLD = 4;

    private record SectionResult(List<ProductItem> products, boolean isFallback) {}

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final WishlistRepository wishlistRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final HomeEventLogRepository homeEventLogRepository;
    private final AdRepository adRepository;
    private final CampaignTargetRepository campaignTargetRepository;

    public HomeResponse getHomeForGuest() {
        List<BestProductItem> best = getBestProducts(BEST_LIMIT_LOGGED_IN, Collections.emptySet());
        List<ProductItem> recommended = getFallbackProducts(Collections.emptySet());
        List<CouponItem> promotions = getPromotions(null);
        List<AdItem> adBanners = getAdBanners(null);
        return new HomeResponse(null, recommended, null, false, null, false, best, promotions, adBanners);
    }

    public HomeResponse getHomeForUser(Long userId) {
        User user = userRepository.findByIdAndIsActive(userId, 1).orElse(null);
        String userName = user != null ? user.getName() : null;

        Set<Long> wishedProductIds = wishlistRepository.findProductIdsByUserId(userId);

        List<ProductItem> recommended = getRecommendedProducts(userId, wishedProductIds);
        SectionResult recentViewed = getRecentViewedProducts(userId, wishedProductIds);
        SectionResult purchased = getPurchasedProducts(userId, wishedProductIds);
        List<BestProductItem> best = getBestProducts(BEST_LIMIT_LOGGED_IN, wishedProductIds);
        List<CouponItem> promotions = getPromotions(userId);
        List<AdItem> adBanners = getAdBanners(userId);

        return new HomeResponse(userName, recommended,
                recentViewed.products(), recentViewed.isFallback(),
                purchased.products(), purchased.isFallback(),
                best, promotions, adBanners);
    }

    private List<ProductItem> getRecommendedProducts(Long userId, Set<Long> wishedProductIds) {
        List<String> categories = homeEventLogRepository.findInterestCategories(userId, RECENT_DAYS, INTEREST_CATEGORY_LIMIT);
        if (!categories.isEmpty()) {
            List<Product> products = productRepository.findByInterestCategoriesAndIsActive(
                    categories, PageRequest.of(0, PRODUCT_LIMIT));
            if (!products.isEmpty()) {
                return toProductItems(products, wishedProductIds);
            }
        }
        return toProductItems(productRepository.findRandomActiveProducts(PRODUCT_LIMIT), wishedProductIds);
    }

    private SectionResult getRecentViewedProducts(Long userId, Set<Long> wishedProductIds) {
        List<Long> productIds = homeEventLogRepository.findRecentViewedProductIds(userId, RECENT_DAYS, PRODUCT_LIMIT);
        if (productIds.size() < SECTION_MIN_THRESHOLD) {
            return new SectionResult(getPersonalizedFallback(userId, wishedProductIds), true);
        }
        List<Product> products = productRepository.findByProductIdInAndIsActive(productIds);
        List<Product> ordered = new ArrayList<>(productIds.size());
        for (Long id : productIds) {
            products.stream().filter(p -> p.getProductId().equals(id)).findFirst().ifPresent(ordered::add);
        }
        return new SectionResult(toProductItems(ordered, wishedProductIds), false);
    }

    private SectionResult getPurchasedProducts(Long userId, Set<Long> wishedProductIds) {
        List<Product> products = productRepository.findPurchasedProductsByUserId(userId, PRODUCT_LIMIT);
        if (products.size() < SECTION_MIN_THRESHOLD) {
            return new SectionResult(getPersonalizedFallback(userId, wishedProductIds), true);
        }
        return new SectionResult(toProductItems(products, wishedProductIds), false);
    }

    private List<ProductItem> getPersonalizedFallback(Long userId, Set<Long> wishedProductIds) {
        Set<String> categories = new LinkedHashSet<>();
        categories.addAll(homeEventLogRepository.findInterestCategories(userId, RECENT_DAYS, INTEREST_CATEGORY_LIMIT));
        categories.addAll(wishlistRepository.findCategoriesByUserId(userId));
        categories.addAll(cartRepository.findCategoriesByUserId(userId));

        if (!categories.isEmpty()) {
            List<Product> products = productRepository.findByInterestCategoriesAndIsActive(
                    new ArrayList<>(categories), PageRequest.of(0, PRODUCT_LIMIT));
            if (!products.isEmpty()) {
                return toProductItems(products, wishedProductIds);
            }
        }
        return toProductItems(productRepository.findRandomActiveProducts(PRODUCT_LIMIT), wishedProductIds);
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
        if (userId == null) {
            return couponRepository.findWelcomeCoupons().stream()
                    .map(c -> toCouponItem(c, false))
                    .toList();
        }

        List<Coupon> coupons = campaignTargetRepository.findDownloadableCouponsByUserId(userId);
        LocalDateTime now = LocalDateTime.now();

        return coupons.stream()
                .filter(c -> c.getExpiredAt() == null
                        || (c.getCreatedAt() != null && c.getCreatedAt().plusDays(c.getExpiredAt()).isAfter(now)))
                .filter(c -> !userCouponRepository.existsByUserIdAndCouponId(userId, c.getId()))
                .map(c -> toCouponItem(c, false))
                .limit(PROMOTION_LIMIT)
                .toList();
    }

    private CouponItem toCouponItem(Coupon c, boolean hasReceived) {
        String expiredAt = null;
        if (c.getExpiredAt() != null && c.getCreatedAt() != null) {
            LocalDate expiryDate = c.getCreatedAt().plusDays(c.getExpiredAt()).toLocalDate();
            expiredAt = expiryDate.toString();
        }
        return new CouponItem(c.getId(), c.getName(),
                c.getDiscountAmount() != null ? c.getDiscountAmount() : 0,
                c.getDiscountType() != null ? c.getDiscountType().name() : null,
                expiredAt, hasReceived);
    }

    private List<AdItem> getAdBanners(Long userId) {
        if (userId != null) {
            List<AD> targeted = campaignTargetRepository.findTargetedAdsByUserId(
                    userId, Pageable.unpaged());
            if (!targeted.isEmpty()) {
                return targeted.stream().map(this::toAdItem).toList();
            }
        }
        return adRepository.findRandomAds(3).stream().map(this::toAdItem).toList();
    }

    private AdItem toAdItem(AD ad) {
        return new AdItem(
                ad.getAdId(),
                ad.getAdName(),
                ad.getTargetType().name(),
                ad.getProduct() != null ? ad.getProduct().getProductId() : null,
                ad.getProduct() != null ? ad.getProduct().getName() : null,
                ad.getProduct() != null ? ad.getProduct().getImageUrl() : null,
                ad.getCategory() != null ? ad.getCategory().name() : null,
                ad.getKeyword()
        );
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
