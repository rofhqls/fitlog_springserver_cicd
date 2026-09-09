package com.fastcam.spserver.dto;

import com.fastcam.spserver.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MemberDto extends User {
    private int num;
    private String id;
    private String pass;
    private String name;
    private String phone;
    private String email;
    private String profileImg;
    private String provider;
    private List<String> role_names = new ArrayList<>();

    public MemberDto(Member member, List<String> roleNames) {
        super(member.getId(), member.getPass(), roleNames.stream().map(
                role-> new SimpleGrantedAuthority("Role_" + role))
                .collect(Collectors.toList())
        );

        this.num = member.getNum();
        this.id = member.getId();
        this.pass = member.getPass();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.email = member.getEmail();
        this.profileImg = member.getProfileImg();
        this.provider = member.getProvider();
        this.role_names = roleNames;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("num", num);
        dataMap.put("id", id);
        dataMap.put("pass",pass);
        dataMap.put("name", name);
        dataMap.put("phone", phone);
        dataMap.put("email", email);
        dataMap.put("profileImg", profileImg);
        dataMap.put("provider", provider);
        dataMap.put("role_names", role_names);

        return dataMap;

    }

}

