package com.example.couphoneserver.repository;

import com.example.couphoneserver.domain.CouponItemStatus;
import com.example.couphoneserver.domain.entity.CouponItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponItemRepository extends JpaRepository<CouponItem, Long> {
    CouponItem findByMemberIdAndBrandIdAndStatus(Long mid, Long bid, CouponItemStatus status);
    CouponItem findByMemberIdAndBrandId(Long mid, Long bid);
    List<CouponItem> findAllByMemberIdAndBrandId(Long mid, Long bid);
}
