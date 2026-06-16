package com.web.ecommerce.domain.product.repository;

import com.web.ecommerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNaverProductId(String naverProductId);

    @Query("SELECT p.naverProductId FROM Product p WHERE p.naverProductId IS NOT NULL")
    List<String> findAllNaverProductIds();

    @Query("SELECT MAX(p.createdAt) FROM Product p")
    Optional<LocalDateTime> findLastSyncedAt();

    Page<Product> findByIsActive(int isActive, Pageable pageable);

    Page<Product> findByIsActiveAndProductCategory(int isActive, String productCategory, Pageable pageable);

    Page<Product> findByIsActiveAndNameContainingIgnoreCase(int isActive, String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productCategory = :category AND p.productId <> :excludeId AND p.isActive = 1 ORDER BY p.createdAt DESC")
    List<Product> findRelatedProducts(@Param("category") String category, @Param("excludeId") Long excludeId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = 1 AND (p.subCategory IN :categories OR p.productCategory IN :categories) ORDER BY p.createdAt DESC")
    List<Product> findByInterestCategoriesAndIsActive(@Param("categories") List<String> categories, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = 1 AND (p.subCategory IN :categories OR p.productCategory IN :categories) AND p.productId NOT IN :excludeIds ORDER BY p.createdAt DESC")
    List<Product> findByInterestCategoriesAndIsActiveExcluding(@Param("categories") List<String> categories, @Param("excludeIds") List<Long> excludeIds, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productId IN :ids AND p.isActive = 1")
    List<Product> findByProductIdInAndIsActive(@Param("ids") List<Long> ids);

    @Query(value = """
            SELECT p.* FROM product p
            INNER JOIN (
                SELECT od.product_id, SUM(od.quantity) AS total_qty
                FROM order_detail od
                JOIN orders o ON o.order_id = od.order_id
                WHERE o.order_date >= NOW() - MAKE_INTERVAL(days => :days)
                GROUP BY od.product_id
                ORDER BY total_qty DESC
                LIMIT :limit
            ) best ON best.product_id = p.product_id
            WHERE p.is_active = 1
            ORDER BY best.total_qty DESC
            """, nativeQuery = true)
    List<Product> findBestSellingProducts(@Param("days") int days, @Param("limit") int limit);

    @Query(value = "SELECT p.* FROM product p WHERE p.is_active = 1 ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomActiveProducts(@Param("limit") int limit);

    @Query(value = """
            SELECT product_id, name, description, min_price, max_price,
                   stock_quantity, product_category, is_active, naver_product_id,
                   image_url, sub_category, brand, mall_name, created_at, updated_at
            FROM (
                SELECT DISTINCT ON (od.product_id)
                       p.product_id, p.name, p.description, p.min_price, p.max_price,
                       p.stock_quantity, p.product_category, p.is_active, p.naver_product_id,
                       p.image_url, p.sub_category, p.brand, p.mall_name,
                       p.created_at, p.updated_at, o.order_date AS latest_order_date
                FROM product p
                JOIN order_detail od ON od.product_id = p.product_id
                JOIN orders o ON o.order_id = od.order_id
                WHERE o.user_id = :userId
                  AND p.is_active = 1
                ORDER BY od.product_id, o.order_date DESC
            ) sub
            ORDER BY latest_order_date DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Product> findPurchasedProductsByUserId(@Param("userId") Long userId, @Param("limit") int limit);
}
