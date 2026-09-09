package com.fastcam.spserver.security.service;

import com.fastcam.spserver.dto.MemberDto;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.entity.MemberRole;
import com.fastcam.spserver.repository.MemberRepository;
import com.fastcam.spserver.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    final MemberRepository mr;
    final MemberRoleRepository mrr;

    // 로그인 요청이 오면 가장 먼저 호출되는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername call - username : " + username + " ------");

        Member member = mr.findById(username);
        if(member == null) {
            throw new UsernameNotFoundException(username + " - User Not Found");
        }

        // username 권한 목록 검색 및 리스트 작성
        List<MemberRole> mrList = mrr.findByMemberNum(member.getNum());
        List<String> mrsList = new ArrayList<>();

        for(MemberRole mr : mrList){
            mrsList.add(mr.getRoleName());
        }
        // security 로그인 처리 방식에 맞춘 Dto 준비
        MemberDto mdto = new MemberDto(
                member,
                mrsList
        );

        return mdto; // UsernamePasswordAuthenticationToken으로 리턴
    }
}
