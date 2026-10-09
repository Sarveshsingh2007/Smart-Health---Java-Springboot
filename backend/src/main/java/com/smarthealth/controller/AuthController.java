package com.smarthealth.controller;

import com.smarthealth.dto.AuthDtos.*;
import com.smarthealth.model.*;
import com.smarthealth.repository.UserRepository;
import com.smarthealth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    @Autowired UserRepository users; @Autowired PasswordEncoder encoder; @Autowired JwtService jwt;
    @Value("${app.admin.email}") String adminEmail; @Value("${app.admin.password}") String adminPassword;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest r){
        if(users.findByEmail(r.email()).isPresent()) throw new RuntimeException("Email already registered");
        Role role;
        try{role=Role.valueOf(r.role()==null?"PATIENT":r.role().toUpperCase());}catch(Exception e){role=Role.PATIENT;}
        if(role==Role.ADMIN) throw new RuntimeException("Admin registration is not allowed");
        User u=User.builder().name(r.name()).email(r.email()).password(encoder.encode(r.password())).phone(r.phone())
            .role(role).organizationName(r.organizationName()).address(r.address()).build();
        u=users.save(u);
        return response(u);
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest r){
        if(r.email().equalsIgnoreCase(adminEmail) && r.password().equals(adminPassword)){
            User admin=users.findByEmail(adminEmail).orElseGet(()->users.save(User.builder().name("System Admin").email(adminEmail).password(encoder.encode(adminPassword)).phone("N/A").role(Role.ADMIN).build()));
            return response(admin);
        }
        User u=users.findByEmail(r.email()).orElseThrow(()->new RuntimeException("Invalid credentials"));
        if(!encoder.matches(r.password(),u.getPassword())) throw new RuntimeException("Invalid credentials");
        return response(u);
    }
    private AuthResponse response(User u){return new AuthResponse(jwt.generate(u.getEmail(),u.getRole().name()),u.getId(),u.getName(),u.getEmail(),u.getRole().name());}
}
