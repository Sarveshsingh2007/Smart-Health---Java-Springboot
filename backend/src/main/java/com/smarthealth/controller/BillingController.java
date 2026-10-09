package com.smarthealth.controller;

import com.smarthealth.dto.BillRequest;
import com.smarthealth.dto.MedicineLine;

import com.smarthealth.model.Appointment;
import com.smarthealth.model.Bill;
import com.smarthealth.model.BillMedicine;
import com.smarthealth.model.Medicine;
import com.smarthealth.model.Role;
import com.smarthealth.model.User;

import com.smarthealth.repository.AppointmentRepository;
import com.smarthealth.repository.BillRepository;
import com.smarthealth.repository.MedicineRepository;
import com.smarthealth.repository.UserRepository;

import com.smarthealth.service.EmailService;
import com.smarthealth.service.PdfService;
import com.smarthealth.service.WhatsAppService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/billing")
public class BillingController {


    @Autowired
    private UserRepository users;


    @Autowired
    private BillRepository bills;


    @Autowired
    private AppointmentRepository apps;


    @Autowired
    private MedicineRepository meds;


    @Autowired
    private PdfService pdf;


    @Autowired
    private EmailService email;


    @Autowired
    private WhatsAppService whatsapp;



    // ============================================================
    // GET MEDICINES BY DISEASE
    // ============================================================

    @GetMapping("/medicines")
    public List<Medicine> medicines(
            @RequestParam String disease
    ) {

        return meds.findByDiseaseIgnoreCase(disease);
    }



    // ============================================================
    // CREATE HOSPITAL BILL
    // ============================================================

    @PostMapping
    public Bill create(
            @RequestBody BillRequest r,
            Authentication auth
    ) {


        // --------------------------------------------------------
        // Get logged-in hospital
        // --------------------------------------------------------

        User hospital =
                getAuthenticatedUser(auth);


        // --------------------------------------------------------
        // Make sure logged-in user is a hospital
        // --------------------------------------------------------

        if (hospital.getRole() != Role.HOSPITAL) {

            throw new RuntimeException(
                    "Only hospital users can create bills"
            );
        }



        // --------------------------------------------------------
        // Get patient
        // --------------------------------------------------------

        if (r.patientId() == null) {

            throw new RuntimeException(
                    "Patient is required"
            );
        }


        User patient =
                users.findById(r.patientId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found"
                                )
                        );



        // --------------------------------------------------------
        // Get appointment if provided
        // --------------------------------------------------------

        Appointment appointment = null;


        if (r.appointmentId() != null) {

            appointment =
                    apps.findById(r.appointmentId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Appointment not found"
                                    )
                            );


            // ----------------------------------------------------
            // Make sure appointment belongs to this hospital
            // ----------------------------------------------------

            if (
                    appointment.getHospital() == null
                            ||
                            !appointment.getHospital()
                                    .getId()
                                    .equals(hospital.getId())
            ) {

                throw new RuntimeException(
                        "This appointment does not belong to your hospital"
                );
            }
        }



        // ========================================================
        // CALCULATE SERVICE SUBTOTAL
        // ========================================================

        double subtotal = 0.0;


        subtotal += n(r.icuCharges());

        subtotal += n(r.mriCharges());

        subtotal += n(r.doctorCharges());

        subtotal += n(r.roomCharges());

        subtotal += n(r.labCharges());

        subtotal += n(r.otherCharges());



        // ========================================================
        // MEDICINES
        // ========================================================

        List<BillMedicine> medicineLines =
                new ArrayList<>();


        if (r.medicines() != null) {

            for (MedicineLine medicine :
                    r.medicines()) {


                // ------------------------------------------------
                // Quantity
                // ------------------------------------------------

                int quantity =
                        medicine.quantity() == null
                                ? 1
                                : medicine.quantity();



                // ------------------------------------------------
                // Unit price
                // ------------------------------------------------

                double unitPrice =
                        n(medicine.unitPrice());



                // ------------------------------------------------
                // Medicine total
                // ------------------------------------------------

                double medicineTotal =
                        unitPrice * quantity;



                // ------------------------------------------------
                // Create BillMedicine
                // ------------------------------------------------

                BillMedicine billMedicine =
                        BillMedicine.builder()

                                .name(
                                        medicine.name()
                                )

                                .quantity(
                                        quantity
                                )

                                .unitPrice(
                                        unitPrice
                                )

                                .lineTotal(
                                        medicineTotal
                                )

                                .customMedicine(
                                        Boolean.TRUE.equals(
                                                medicine.customMedicine()
                                        )
                                )

                                .build();


                medicineLines.add(
                        billMedicine
                );


                // Add medicine amount
                // to subtotal

                subtotal +=
                        medicineTotal;
            }
        }



        // ========================================================
        // TAX
        // ========================================================

        double tax =
                n(r.tax());



        // ========================================================
        // FINAL TOTAL
        // ========================================================

        double total =
                subtotal + tax;



        // ========================================================
        // CREATE BILL
        // ========================================================

        Bill bill =
                Bill.builder()

                        .billNumber(
                                "SHA-" +
                                        System.currentTimeMillis()
                        )

                        .patient(
                                patient
                        )

                        .hospital(
                                hospital
                        )

                        .appointment(
                                appointment
                        )

                        .icuCharges(
                                n(r.icuCharges())
                        )

                        .mriCharges(
                                n(r.mriCharges())
                        )

                        .doctorCharges(
                                n(r.doctorCharges())
                        )

                        .roomCharges(
                                n(r.roomCharges())
                        )

                        .labCharges(
                                n(r.labCharges())
                        )

                        .otherCharges(
                                n(r.otherCharges())
                        )

                        .medicines(
                                medicineLines
                        )

                        .subtotal(
                                subtotal
                        )

                        .tax(
                                tax
                        )

                        .total(
                                total
                        )

                        .notes(
                                r.notes()
                        )

                        .build();



        // ========================================================
        // SAVE BILL TO POSTGRESQL
        // ========================================================

        bill =
                bills.save(bill);



        // ========================================================
        // EMAIL + PDF
        // ========================================================

        String text =
                "Hospital Bill " +
                        bill.getBillNumber() +

                        "\nPatient: " +
                        patient.getName() +

                        "\nEmail: " +
                        patient.getEmail() +

                        "\nContact: " +
                        patient.getPhone() +

                        "\nTotal: ₹" +
                        String.format(
                                "%.2f",
                                total
                        ) +

                        "\nHospital: " +
                        hospital.getOrganizationName() +

                        "\n\nThe detailed PDF hospital bill is attached.";


        try {

            // ----------------------------------------------------
            // Generate PDF
            // ----------------------------------------------------

            byte[] pdfBytes =
                    pdf.billPdf(bill);



            // ----------------------------------------------------
            // Send PDF through email
            // ----------------------------------------------------

            email.sendWithPdf(

                    patient.getEmail(),

                    "Hospital Bill - " +
                            bill.getBillNumber(),

                    text,

                    pdfBytes,

                    bill.getBillNumber() +
                            ".pdf"
            );


        } catch (Exception ex) {

            // ----------------------------------------------------
            // If PDF generation fails,
            // send normal email instead.
            // ----------------------------------------------------

            ex.printStackTrace();

            email.send(

                    patient.getEmail(),

                    "Hospital Bill - " +
                            bill.getBillNumber(),

                    text
            );
        }



        // ========================================================
        // WHATSAPP
        // ========================================================

        whatsapp.sendText(

                patient.getPhone(),

                text
        );



        // ========================================================
        // RETURN CREATED BILL
        // ========================================================

        return bill;
    }



    // ============================================================
    // PATIENT'S BILLS
    // ============================================================

    @GetMapping("/my")
    public List<Bill> my(
            Authentication auth
    ) {


        User patient =
                getAuthenticatedUser(auth);


        return bills.findByPatientIdOrderByIdDesc(
                patient.getId()
        );
    }



    // ============================================================
    // DOWNLOAD BILL PDF
    // ============================================================

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(
            @PathVariable Long id,
            Authentication auth
    ) throws Exception {


        // --------------------------------------------------------
        // Logged-in user
        // --------------------------------------------------------

        User user =
                getAuthenticatedUser(auth);



        // --------------------------------------------------------
        // Find bill
        // --------------------------------------------------------

        Bill bill =
                bills.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bill not found"
                                )
                        );



        // ========================================================
        // ACCESS CONTROL
        // ========================================================

        boolean isHospital =
                user.getRole() == Role.HOSPITAL
                        &&
                        bill.getHospital() != null
                        &&
                        bill.getHospital()
                                .getId()
                                .equals(user.getId());



        boolean isPatient =
                user.getRole() == Role.PATIENT
                        &&
                        bill.getPatient() != null
                        &&
                        bill.getPatient()
                                .getId()
                                .equals(user.getId());



        boolean isAdmin =
                user.getRole() == Role.ADMIN;



        boolean allowed =
                isHospital
                        ||
                        isPatient
                        ||
                        isAdmin;



        if (!allowed) {

            throw new ResponseStatusException(

                    HttpStatus.FORBIDDEN,

                    "You are not allowed to access this bill"
            );
        }



        // ========================================================
        // GENERATE PDF
        // ========================================================

        byte[] pdfBytes =
                pdf.billPdf(bill);



        // ========================================================
        // RETURN PDF
        // ========================================================

        return ResponseEntity
                .ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,

                        "attachment; filename=" +
                                bill.getBillNumber() +
                                ".pdf"
                )

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .body(
                        pdfBytes
                );
    }



    // ============================================================
    // GET AUTHENTICATED USER
    // ============================================================

    private User getAuthenticatedUser(
            Authentication auth
    ) {


        // --------------------------------------------------------
        // Check authentication
        // --------------------------------------------------------

        if (
                auth == null
                        ||
                        !auth.isAuthenticated()
        ) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }



        // --------------------------------------------------------
        // Get principal
        // --------------------------------------------------------

        Object principal =
                auth.getPrincipal();



        // --------------------------------------------------------
        // CASE 1:
        // Principal is our User entity
        // --------------------------------------------------------

        if (principal instanceof User user) {

            return user;
        }



        // --------------------------------------------------------
        // CASE 2:
        // Principal is Spring Security UserDetails
        // --------------------------------------------------------

        if (
                principal instanceof UserDetails userDetails
        ) {

            String email =
                    userDetails.getUsername();


            return users
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found for email: "
                                            + email
                            )
                    );
        }



        // --------------------------------------------------------
        // CASE 3:
        // Fallback to Authentication name
        // --------------------------------------------------------

        String email =
                auth.getName();


        return users
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found for authentication: "
                                        + email
                        )
                );
    }



    // ============================================================
    // NULL-SAFE DOUBLE
    // ============================================================

    private double n(
            Double value
    ) {

        return value == null
                ? 0.0
                : value;
    }
}