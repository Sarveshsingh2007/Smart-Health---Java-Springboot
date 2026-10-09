package com.smarthealth.controller;

import com.smarthealth.model.*;
import com.smarthealth.repository.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    UserRepository users;

    @Autowired
    AppointmentRepository apps;

    @Autowired
    BillRepository bills;


    // =========================================================
    // LIST HOSPITALS
    // =========================================================

    @GetMapping("/list")
    public List<User> hospitals() {

        return users.findAll()
                .stream()
                .filter(u -> u.getRole() == Role.HOSPITAL)
                .toList();
    }


    // =========================================================
    // HOSPITAL DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(
            Authentication auth
    ) {

        User h = getAuthenticatedHospital(auth);


        // -----------------------------------------------------
        // GET APPOINTMENTS FOR THIS HOSPITAL
        // -----------------------------------------------------

        List<Appointment> appointments =
                apps.findAllByOrderByIdDesc()
                        .stream()
                        .filter(a ->
                                a.getHospital() != null
                                        && a.getHospital()
                                        .getId()
                                        .equals(h.getId())
                        )
                        .toList();


        // -----------------------------------------------------
        // GET BILLS FOR THIS HOSPITAL
        // -----------------------------------------------------

        List<Bill> hospitalBills =
                bills.findAllByOrderByIdDesc()
                        .stream()
                        .filter(b ->
                                b.getHospital() != null
                                        && b.getHospital()
                                        .getId()
                                        .equals(h.getId())
                        )
                        .toList();


        // -----------------------------------------------------
        // RESPONSE
        // -----------------------------------------------------

        return Map.of(
                "hospital", h,
                "appointments", appointments,
                "bills", hospitalBills,
                "appointmentCount", appointments.size(),
                "billCount", hospitalBills.size()
        );
    }


    // =========================================================
    // GET AUTHENTICATED HOSPITAL
    // =========================================================

    private User getAuthenticatedHospital(
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        Object principal = auth.getPrincipal();


        // -----------------------------------------------------
        // CASE 1:
        // Principal itself is our User entity
        // -----------------------------------------------------

        if (principal instanceof User user) {

            return user;
        }


        // -----------------------------------------------------
        // CASE 2:
        // Principal is Spring Security UserDetails
        // -----------------------------------------------------

        if (principal instanceof UserDetails userDetails) {

            String email = userDetails.getUsername();

            return users.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Hospital not found for email: "
                                            + email
                            )
                    );
        }


        // -----------------------------------------------------
        // CASE 3:
        // Try Authentication name
        // -----------------------------------------------------

        String email = auth.getName();
        return users.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Hospital not found for authentication: "
                                        + email
                        )
                );
    }
}