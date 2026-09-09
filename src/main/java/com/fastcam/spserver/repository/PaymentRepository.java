package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByMemberOrderByIndateDesc(Member member);

    List<Payment> findByMemberNumOrderByIndateDesc(int mnum);
}
