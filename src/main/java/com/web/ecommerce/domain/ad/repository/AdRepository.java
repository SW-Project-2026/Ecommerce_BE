package com.web.ecommerce.domain.ad.repository;

import com.web.ecommerce.domain.ad.entity.AD;
import com.web.ecommerce.domain.ad.enums.AdCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<AD, Long> {

}
