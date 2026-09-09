package com.fastcam.spserver.security.filter;

import com.fastcam.spserver.dto.MemberDto;
import com.fastcam.spserver.entity.Member;
import com.fastcam.spserver.security.util.CustomJWTException;
import com.fastcam.spserver.security.util.JWTUtil;
import com.fastcam.spserver.service.MemberService;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class JWTCheckFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeaderStr = request.getHeader("Authorization");
        try {
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);
            int num= (Integer)claims.get("num");
            String id=(String)claims.get("id");
            String pass = (String) claims.get("pass");
            String name = (String) claims.get("name");
            String phone = (String) claims.get("phone");
            String email = (String) claims.get("email");
            String profileImg = (String) claims.get("profileImg");
            String provider = (String) claims.get("provider");
            List<String> role_names  = (List<String>) claims.get("role_names");

            Member m = new Member();
            m.setNum(num);
            m.setId(id);
            m.setPass(pass);
            m.setName(name);
            m.setPhone(phone);
            m.setEmail(email);
            m.setProfileImg(profileImg);
            m.setProvider(provider);

            MemberDto mdto = new MemberDto( m,role_names );

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(mdto, pass , mdto.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (CustomJWTException e) {
            System.out.println("JWT Check Error..............");
            System.out.println(e.getMessage());
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        System.out.println("check uri.........." + path);

        if (path.startsWith("/member/loginLocal"))
            return true;
        if(path.startsWith("/image"))
            return true;
        if(path.startsWith("/member/getMemberByNum"))
            return true;
        if(path.startsWith("/member/fileupload"))
            return true;
        if(path.startsWith("/member/idcheck"))
            return true;
        if(path.startsWith("/member/join"))
            return true;
        if(path.startsWith("/community/getLikeList"))
            return true;
        if(path.startsWith("/community/getPostList"))
            return true;
        if(path.startsWith("/community/getReplyList"))
            return true;
        if(path.startsWith("/notice/getAllList"))
            return true;
        if(path.startsWith("/notice/getNotice/"))
            return true;
        if(path.startsWith("/member/kakaoinfoUpdate"))
            return true;
        if(path.startsWith("/member/kakaostart"))
            return true;
        if(path.startsWith("/member/kakaoLogin"))
            return true;
        if(path.startsWith("/member/refresh"))
            return true;
        if(path.startsWith("/favicon.ico"))
            return true;
        if(path.startsWith("/member/resetPass"))
            return true;
        if(path.startsWith("/member/findPassCheck"))
            return true;
        if(path.startsWith("/member/findId"))
            return true;
        if(path.startsWith("/chat"))
            return true;
        if(path.startsWith("/member/sendSMS"))
            return true;
        if(path.startsWith("/member/confirmSMSCode"))
            return true;
        if(path.startsWith("/member/sendEmail"))
            return true;
        if(path.startsWith("/member/confirmEmailCode"))
            return true;
        if(path.startsWith("/charge"))
            return true;
        return false;
    }
}
