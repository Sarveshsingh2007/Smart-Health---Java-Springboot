package com.smarthealth.controller;

import com.smarthealth.model.Role;
import com.smarthealth.model.User;
import com.smarthealth.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalDirectoryController {
    @Autowired UserRepository users;

    @GetMapping
    public List<User> hospitals() {
        return users.findAll().stream()
            .filter(u -> u.getRole() == Role.HOSPITAL)
            .map(u -> User.builder().id(u.getId()).name(u.getName()).email(u.getEmail())
                .phone(u.getPhone()).role(u.getRole()).organizationName(u.getOrganizationName())
                .address(u.getAddress()).build())
            .toList();
    }
}
