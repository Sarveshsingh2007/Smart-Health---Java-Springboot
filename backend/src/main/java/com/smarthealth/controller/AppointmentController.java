package com.smarthealth.controller;

import com.smarthealth.dto.*;
import com.smarthealth.model.*;
import com.smarthealth.repository.*;
import com.smarthealth.service.EmailService;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    UserRepository users;

    @Autowired
    AppointmentRepository apps;

    @Autowired
    EmailService mail;


    // =========================================================
    // CREATE APPOINTMENT
    // =========================================================

    @PostMapping
    public Appointment create(
            @RequestBody AppointmentRequest r,
            Authentication auth
    ) {

        if (auth == null) {
            throw new RuntimeException(
                    "User is not authenticated. Please login again."
            );
        }

        // Your authentication principal is already User
        User patient = getAuthenticatedUser(auth);

        System.out.println(
                "Logged-in patient: " + patient.getEmail()
        );


        // =====================================================
        // FIND HOSPITAL
        // =====================================================

        User hospital = null;

        if (r.hospitalId() != null) {

            hospital = users
                    .findById(r.hospitalId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Hospital not found with ID: "
                                            + r.hospitalId()
                            )
                    );
        }


        // =====================================================
        // VALIDATE SYMPTOMS
        // =====================================================

        if (r.symptoms() == null
                || r.symptoms().trim().isEmpty()) {

            throw new RuntimeException(
                    "Symptoms cannot be empty."
            );
        }


        // =====================================================
        // CREATE APPOINTMENT
        // =====================================================

        Appointment a = Appointment.builder()
                .patient(patient)
                .hospital(hospital)
                .symptoms(r.symptoms())
                .medicalHistory(r.medicalHistory())
                .preferredDate(r.preferredDate())
                .preferredTime(r.preferredTime())
                .status(AppointmentStatus.PENDING)
                .build();

        a = apps.save(a);


        // =====================================================
        // SEND EMAIL TO HOSPITAL
        // =====================================================

        if (hospital != null) {

            String message =
                    "New patient appointment request from "
                            + patient.getName()
                            + ".\n\n"

                            + "Patient Email: "
                            + patient.getEmail()
                            + "\n"

                            + "Symptoms: "
                            + a.getSymptoms()
                            + "\n"

                            + "Medical History: "
                            + a.getMedicalHistory()
                            + "\n"

                            + "Preferred Date: "
                            + a.getPreferredDate()
                            + "\n"

                            + "Preferred Time: "
                            + a.getPreferredTime()
                            + "\n\n"

                            + "Login to the hospital dashboard "
                            + "to accept or reject the appointment.";

            mail.send(
                    hospital.getEmail(),
                    "New Appointment Request",
                    message
            );
        }

        return a;
    }


    // =========================================================
    // MY APPOINTMENTS
    // =========================================================

    @GetMapping("/my")
    public List<Appointment> my(
            Authentication auth
    ) {

        if (auth == null) {
            throw new RuntimeException(
                    "User is not authenticated."
            );
        }

        User patient = getAuthenticatedUser(auth);

        System.out.println(
                "Loading appointments for: "
                        + patient.getEmail()
        );

        return apps.findByPatientIdOrderByIdDesc(
                patient.getId()
        );
    }


    // =========================================================
    // ACCEPT / REJECT APPOINTMENT
    // =========================================================

    @PatchMapping("/{id}/status")
    public Appointment status(
            @PathVariable Long id,
            @RequestBody StatusRequest r,
            Authentication auth
    ) {

        if (auth == null) {
            throw new RuntimeException(
                    "Hospital is not authenticated."
            );
        }


        // Your authentication principal is already User
        User hospital = getAuthenticatedUser(auth);

        System.out.println(
                "Logged-in hospital: "
                        + hospital.getEmail()
        );


        // =====================================================
        // FIND APPOINTMENT
        // =====================================================

        Appointment a = apps
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found with ID: "
                                        + id
                        )
                );


        // =====================================================
        // CHECK APPOINTMENT OWNERSHIP
        // =====================================================

        if (a.getHospital() == null
                || !a.getHospital()
                .getId()
                .equals(hospital.getId())) {

            throw new RuntimeException(
                    "This appointment does not belong "
                            + "to the logged-in hospital."
            );
        }


        // =====================================================
        // UPDATE STATUS
        // =====================================================

        a.setStatus(
                AppointmentStatus.valueOf(
                        r.status().toUpperCase()
                )
        );


        // =====================================================
        // DOCTOR INFORMATION
        // =====================================================

        a.setDoctorName(
                r.doctorName()
        );

        a.setSpecialist(
                r.specialist()
        );


        // =====================================================
        // CONFIRMED DATE / TIME
        // =====================================================

        a.setConfirmedDate(
                r.confirmedDate()
        );

        a.setConfirmedTime(
                r.confirmedTime()
        );


        // =====================================================
        // HOSPITAL NOTES
        // =====================================================

        a.setHospitalNotes(
                r.hospitalNotes()
        );


        a = apps.save(a);


        // =====================================================
        // SEND EMAIL TO PATIENT
        // =====================================================

        String msg =
                "Your appointment request is "
                        + a.getStatus()
                        + ".\n\n"

                        + "Hospital: "
                        + hospital.getOrganizationName()
                        + "\n"

                        + "Doctor: "
                        + a.getDoctorName()
                        + "\n"

                        + "Specialist: "
                        + a.getSpecialist()
                        + "\n"

                        + "Date: "
                        + a.getConfirmedDate()
                        + "\n"

                        + "Time: "
                        + a.getConfirmedTime()
                        + "\n"

                        + "Notes: "
                        + a.getHospitalNotes();


        mail.send(
                a.getPatient().getEmail(),
                "Appointment " + a.getStatus(),
                msg
        );


        return a;
    }


    // =========================================================
    // GET AUTHENTICATED USER
    // =========================================================

    private User getAuthenticatedUser(
            Authentication auth
    ) {

        Object principal = auth.getPrincipal();

        if (principal instanceof User) {

            return (User) principal;
        }

        throw new RuntimeException(
                "Authenticated principal is not a Smart Health User."
        );
    }
}