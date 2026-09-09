package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findById(String id);

    Member findByNum(int num);

    void deleteById(String id);

    Member findByNameAndPhone(String name, String phone);

    int countByIdAndNameAndPhone(String id, String name, String phone);

    Member findBySnsid(String id);

    Member findByIdAndEmail(String id, String email);
}
