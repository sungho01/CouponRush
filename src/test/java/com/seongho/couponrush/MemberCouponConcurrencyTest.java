package com.seongho.couponrush;

import com.seongho.couponrush.entity.Coupon;
import com.seongho.couponrush.entity.Member;
import com.seongho.couponrush.repository.CouponRepository;
import com.seongho.couponrush.repository.MemberCouponRepository;
import com.seongho.couponrush.repository.MemberRepository;
import com.seongho.couponrush.service.MemberCouponService;
import com.seongho.couponrush.service.RedisCouponIssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class MemberCouponConcurrencyTest  {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private RedisCouponIssueService redisCouponIssueService;

    @Test
    void issueCouponConcurrencyTest() throws InterruptedException{

        String testRunId = String.valueOf(System.currentTimeMillis());

        int couponQuantity = 100;
        int memberCount = 200;

        Coupon coupon = Coupon.create(
                "동시성 테스트 쿠폰",
                couponQuantity,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        couponRepository.save(coupon);

        List<String> loginIds = new ArrayList<>();

        for(int i = 0; i < memberCount; i++){
            Member member = Member.createUser(
                    "testId"+ testRunId + i,
                    "1234",
                    "user" + i,
                    "test" + testRunId + i + "@gmail.com",
                    LocalDate.of(1998, 1, 1)
            );

            memberRepository.save(member);

            loginIds.add(member.getLoginId());
        }

        int threadCount = 32;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(memberCount);

        for(String loginId : loginIds){
            executorService.submit(() ->{
                try {
                    startLatch.await();
                    //memberCouponService.issueCoupon(loginId, coupon.getId());
                    //memberCouponService.issueCouponWithPessimisticLock(loginId, coupon.getId());
                    redisCouponIssueService.issueCouponWithRedisLock(loginId, coupon.getId());
                } catch (Exception e){
                    System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        long startTime = System.nanoTime();

        startLatch.countDown();
        doneLatch.await();

        long endTime = System.nanoTime();

        executorService.shutdown();

        long executionTime = (endTime - startTime) / 1_000_000;

        System.out.println("실행 시간 = " + executionTime + "ms");

        long issuedCount = memberCouponRepository.countByCoupon_Id(coupon.getId());
        Coupon resultCoupon = couponRepository.findById(coupon.getId()).orElseThrow();

        System.out.println("발급 개수 = " + issuedCount);
        System.out.println("남은 수량 = " + resultCoupon.getRemainingQuantity());
    }

}
