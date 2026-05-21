package com.web.ecommerce.domain.ad.repository;

import com.web.ecommerce.domain.ad.entity.AD;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdRepository extends JpaRepository<AD, Long> {

  @Query("SELECT a FROM AD a WHERE a.adId > :cursor ORDER BY a.adId ASC")
  List<AD> findByCursor(Long cursor, Pageable pageable);
}
