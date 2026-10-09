package com.smarthealth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bill_medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer quantity;

    private Double unitPrice;

    private Double lineTotal;

    private boolean customMedicine;
}