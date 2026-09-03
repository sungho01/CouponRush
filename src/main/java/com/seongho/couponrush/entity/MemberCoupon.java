package com.seongho.couponrush.entity;

import com.seongho.couponrush.enums.MemberCouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "coupon_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberCouponStatus status;
    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;

    private MemberCoupon(Member member, Coupon coupon){
        this.member = member;
        this.coupon = coupon;
        this.status = MemberCouponStatus.ISSUED;
        this.issuedAt = LocalDateTime.now();
    }

    public static MemberCoupon create(Member member, Coupon coupon) {
        return new MemberCoupon(member, coupon);

    }

    public void used(){
        this.status = MemberCouponStatus.USED;
        this.usedAt = LocalDateTime.now();
    }

}
