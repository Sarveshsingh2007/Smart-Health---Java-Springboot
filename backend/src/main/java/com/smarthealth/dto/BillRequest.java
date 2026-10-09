package com.smarthealth.dto;

import java.util.List;

public record BillRequest(
        Long appointmentId,
        Long patientId,
        Double icuCharges,
        Double mriCharges,
        Double doctorCharges,
        Double roomCharges,
        Double labCharges,
        Double otherCharges,
        Double tax,
        String notes,
        List<MedicineLine> medicines
) {
}