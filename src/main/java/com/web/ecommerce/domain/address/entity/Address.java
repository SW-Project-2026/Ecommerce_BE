package com.web.ecommerce.domain.address.entity;

import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Address extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "road_name_address", nullable = false, length = 200)
    private String roadNameAddress;

    @Column(name = "address_detail", nullable = false, length = 200)
    private String addressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void update(String roadNameAddress, String addressDetail) {
        this.roadNameAddress = roadNameAddress;
        this.addressDetail = addressDetail;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
