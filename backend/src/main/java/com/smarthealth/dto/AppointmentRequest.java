package com.smarthealth.dto;
public record AppointmentRequest(
        Long hospitalId,
        String symptoms,
        String medicalHistory,
        String preferredDate,
        String preferredTime,
        Integer age,
        String gender
){}