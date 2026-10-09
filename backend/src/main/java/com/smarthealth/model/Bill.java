package com.smarthealth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @ManyToOne(optional = false)
    private User patient;

    @ManyToOne(optional = false)
    private User hospital;

    @OneToOne
    private Appointment appointment;

    private Double icuCharges = 0.0;

    private Double mriCharges = 0.0;

    private Double doctorCharges = 0.0;

    private Double roomCharges = 0.0;

    private Double labCharges = 0.0;

    private Double otherCharges = 0.0;

    private Double subtotal = 0.0;

    private Double tax = 0.0;

    private Double total = 0.0;

    private String createdAt;

    @Column(length = 1000)
    private String notes;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "bill_id")
    @Builder.Default
    private List<BillMedicine> medicines =
            new ArrayList<>();

    @PrePersist
    void created() {

        if (createdAt == null) {
            createdAt =
                    LocalDateTime.now().toString();
        }
    }
}