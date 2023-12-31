package com.example.couphoneserver.domain.entity;

import com.example.couphoneserver.domain.BrandStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "BRAND")
public class Brand extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;
    private String name;
    private String rewardDescription;
    @Enumerated(EnumType.STRING)
    private BrandStatus status;

    private String brandImageUrl;

    @OneToMany(mappedBy = "brand")
    private List<CouponItem> coupons = new ArrayList<>(); // 해당 브랜드에서 발급한 쿠폰들

    @OneToMany(mappedBy = "brand")
    private List<Store> stores = new ArrayList<>(); // 해당 브랜드의 매장들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
