package com.smarthealth.dto;

public record MedicineLine(
        String name,
        Integer quantity,
        Double unitPrice,
        Boolean customMedicine
) {
}