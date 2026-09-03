package com.seongho.couponrush.entity;

import com.seongho.couponrush.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer totalQuantity;
    @Column(nullable = false)
    private Integer remainingQuantity;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Coupon(
            String name,
            Integer totalQuantity,
            LocalDate startDate,
            LocalDate endDate
            ){
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = totalQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = LocalDateTime.now();
        updateStatus();
    }

    public void updateStatus(){
        LocalDate today = LocalDate.now();

        if(today.isBefore(startDate)){
            this.status = CouponStatus.SCHEDULED;
        }
        else if(today.isAfter(endDate)){
            this.status = CouponStatus.EXPIRED;
        }
        else{
            this.status = CouponStatus.ACTIVE;
        }
    }

    public static Coupon create(
            String name,
            Integer totalQuantity,
            LocalDate startDate,
            LocalDate endDate
    ){
        return new Coupon(name, totalQuantity, startDate, endDate);
    }

    public void decreaseRemainingQuantity(){
        this.remainingQuantity--;
    }
}
