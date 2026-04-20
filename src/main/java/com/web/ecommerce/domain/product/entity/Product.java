package com.web.ecommerce.domain.product.entity;

import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "product_category", length = 50)
    private String productCategory;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "naver_product_id", unique = true, length = 50)
    private String naverProductId;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "sub_category", length = 50)
    private String subCategory;

    @Column(name = "search_keyword", length = 50)
    private String searchKeyword;

    public void update(String name, String description, int price, int stockQuantity,
                       String productCategory, Integer isActive, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.productCategory = productCategory;
        this.isActive = isActive;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }

    public void deactivate() {
        this.isActive = 0;
    }
}
