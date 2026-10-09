package com.smarthealth.dto;
public class AuthDtos {
    public record RegisterRequest(String name,String email,String password,String phone,String role,String organizationName,String address){}
    public record LoginRequest(String email,String password){}
    public record AuthResponse(String token,Long id,String name,String email,String role){}
}
