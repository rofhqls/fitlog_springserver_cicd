package com.fastcam.spserver.repository;

import com.fastcam.spserver.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Integer> {
    List<MemberRole> findByMemberNum(int num);

    void deleteByMemberNum(int mnum);
}
