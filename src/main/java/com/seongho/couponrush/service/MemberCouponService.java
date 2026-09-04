package com.seongho.couponrush.service;

import com.seongho.couponrush.dto.MemberCouponResponseDto;
import com.seongho.couponrush.dto.MessageResponseDto;
import com.seongho.couponrush.entity.Coupon;
import com.seongho.couponrush.entity.Member;
import com.seongho.couponrush.entity.MemberCoupon;
import com.seongho.couponrush.enums.MemberCouponStatus;
import com.seongho.couponrush.repository.CouponRepository;
import com.seongho.couponrush.repository.MemberCouponRepository;
import com.seongho.couponrush.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public MessageResponseDto issueCoupon(String loginId, Long couponId){

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 회원입니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰입니다."));

        if(memberCouponRepository.existsByMember_IdAndCoupon_Id(member.getId(), couponId)){
            throw new IllegalArgumentException("이미 발급받은 쿠폰입니다.");
        }

        if(LocalDate.now().isAfter(coupon.getEndDate())){
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }

        if (coupon.getRemainingQuantity() > 0) {
            coupon.decreaseRemainingQuantity();
        } else {
            throw new IllegalArgumentException("쿠폰 수량이 모두 소진되었습니다.");
        }
        MemberCoupon memberCoupon = MemberCoupon.create(member, coupon);
        memberCouponRepository.save(memberCoupon);

        return new MessageResponseDto("쿠폰이 발급됐습니다.");
    }

    @Transactional
    public MessageResponseDto issueCouponWithPessimisticLock(String loginId, Long couponId){
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 회원입니다."));

        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰입니다."));

        if(memberCouponRepository.existsByMember_IdAndCoupon_Id(member.getId(), couponId)){
            throw new IllegalArgumentException("이미 발급받은 쿠폰입니다.");
        }

        if(LocalDate.now().isAfter(coupon.getEndDate())){
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }

        if (coupon.getRemainingQuantity() > 0) {
            coupon.decreaseRemainingQuantity();
        } else {
            throw new IllegalArgumentException("쿠폰 수량이 모두 소진되었습니다.");
        }
        MemberCoupon memberCoupon = MemberCoupon.create(member, coupon);
        memberCouponRepository.save(memberCoupon);

        return new MessageResponseDto("쿠폰이 발급됐습니다.");
    }

    @Transactional(readOnly = true)
    public Page<MemberCouponResponseDto> getMyCoupons(String loginId, Pageable pageable){
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 회원입니다."));

        Page<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMember_Id(member.getId(), pageable);

        return memberCoupons.map( memberCoupon -> new MemberCouponResponseDto(
                memberCoupon.getId(),
                memberCoupon.getCoupon().getName(),
                memberCoupon.getStatus(),
                memberCoupon.getIssuedAt(),
                memberCoupon.getUsedAt()
        ));
    }

    @Transactional(readOnly = true)
    public MemberCouponResponseDto getMyCoupon(String loginId, Long memberCouponId){
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 회원입니다."));

        MemberCoupon memberCoupon = memberCouponRepository.findByIdAndMember_Id(memberCouponId, member.getId())
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 쿠폰입니다."));

        return new MemberCouponResponseDto(
                memberCoupon.getId(),
                memberCoupon.getCoupon().getName(),
                memberCoupon.getStatus(),
                memberCoupon.getIssuedAt(),
                memberCoupon.getUsedAt()
        );
    }

    @Transactional
    public MessageResponseDto useCoupon(String loginId, Long memberCouponId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원입니다."));

        MemberCoupon memberCoupon = memberCouponRepository.findByIdAndMember_Id(memberCouponId, member.getId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰입니다."));
        Coupon coupon = memberCoupon.getCoupon();

        if(memberCoupon.getStatus() != MemberCouponStatus.ISSUED){
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }
        else{
            LocalDate today = LocalDate.now();

            if (today.isBefore(coupon.getStartDate())) {
                throw new IllegalArgumentException("아직 사용 기간이 시작되지 않은 쿠폰입니다.");
            }

            if (today.isAfter(coupon.getEndDate())) {
                throw new IllegalArgumentException("이미 만료된 쿠폰입니다.");
            }

            memberCoupon.used();
        }

        return new MessageResponseDto("쿠폰이 사용되었습니다.");
    }
}
