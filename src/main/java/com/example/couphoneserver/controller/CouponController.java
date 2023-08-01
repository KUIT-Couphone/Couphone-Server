package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.response.BaseResponse;
import com.example.couphoneserver.dto.coupon.*;
import com.example.couphoneserver.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "coupons", description = "쿠폰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;

    @PostMapping("")
    @Operation(summary = "쿠폰 등록", description = "Request Body에 브랜드 ID, 멤버 ID 넣어주세요!")
    public BaseResponse<PostCouponResponse> postCouponItem(@RequestBody PostCouponRequest request){
        return new BaseResponse<>(couponService.saveCoupon(request));
    }

    @PatchMapping("/stamp/{coupon-id}")
    @Operation(summary = "쿠폰 스탬프 적립", description = "Path Variable로 쿠폰 ID 넣어주세요!")
    public BaseResponse<PatchCouponCountResponse> patchCouponItemCount(@PathVariable("coupon-id") Long couponId) {
        return new BaseResponse<>(couponService.collectStamp(couponId));
    }

    @PatchMapping("/status/{coupon-id}")
    @Operation(summary = "쿠폰 사용하기", description = "Path Variable로 쿠폰 ID 넣어주세요!")
    public BaseResponse<PatchCouponStatusResponse> patchCouponItemStatus(@PathVariable("coupon-id") Long couponId) {
        return new BaseResponse<>(couponService.useCoupon(couponId));
    }
}
