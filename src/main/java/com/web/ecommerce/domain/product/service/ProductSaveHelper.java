package com.web.ecommerce.domain.product.service;

import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductSaveHelper {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Product product) {
        productRepository.save(product);
    }
}
