package com.smarthealth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="medicines")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medicine {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String disease;
    @Column(nullable=false) private String name;
    private String description;
    private String dosage;
    private Double defaultPrice;
}
