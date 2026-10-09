package com.smarthealth.security;

import com.smarthealth.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired JwtService jwt; @Autowired UserRepository users;
    @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        String h=req.getHeader("Authorization");
        if(h!=null && h.startsWith("Bearer ")){
            String token=h.substring(7);
            try{
                if(jwt.valid(token)){
                    String email=jwt.email(token);
                    users.findByEmail(email).ifPresent(u -> {
                        var auth=new UsernamePasswordAuthenticationToken(u,null,List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name())));
                        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
                    });
                }
            }catch(Exception ignored){}
        }
        chain.doFilter(req,res);
    }
}
