package com.seongho.couponrush.service;

import com.seongho.couponrush.dto.CouponCreateRequestDto;
import com.seongho.couponrush.dto.CouponResponseDto;
import com.seongho.couponrush.dto.MessageResponseDto;
import com.seongho.couponrush.entity.Coupon;
import com.seongho.couponrush.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    @Transactional
    public MessageResponseDto createCoupon(CouponCreateRequestDto requestDto){

        if(requestDto.getStartDate().isAfter(requestDto.getEndDate())){
            throw new IllegalArgumentException("시작일은 종료일보다 늦을 수 없습니다.");
        }

        Coupon coupon = Coupon.create(
                requestDto.getName(),
                requestDto.getTotalQuantity(),
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );

        couponRepository.save(coupon);

        return new MessageResponseDto("쿠폰 등록이 완료됐습니다.");
    }

    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getCoupons(Pageable pageable){
        Page<Coupon> coupons = couponRepository.findAll(pageable);

        return coupons.map(coupon -> new CouponResponseDto(
                coupon.getId(),
                coupon.getName(),
                coupon.getTotalQuantity(),
                coupon.getRemainingQuantity(),
                coupon.getStartDate(),
                coupon.getEndDate(),
                coupon.getStatus(),
                coupon.getCreatedAt()
                ));
    }

    @Transactional(readOnly = true)
    public CouponResponseDto getCoupon(Long couponId){
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰입니다."));

        return new CouponResponseDto(
               coupon.getId(),
               coupon.getName(),
               coupon.getTotalQuantity(),
               coupon.getRemainingQuantity(),
               coupon.getStartDate(),
               coupon.getEndDate(),
               coupon.getStatus(),
               coupon.getCreatedAt()
        );
    }
}
