package com.smarthealth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="minor_symptoms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MinorSymptom {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(length=5000) private String description;
    @Column(length=5000) private String medicines;
    @Column(length=5000) private String food;
    @Column(length=5000) private String precautions;
    @Column(length=3000) private String whenToDoctor;
}
