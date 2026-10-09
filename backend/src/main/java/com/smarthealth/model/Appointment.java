package com.smarthealth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="appointments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private User patient;
    private Integer age;
    private String gender;
    @ManyToOne private User hospital;
    @Column(length=3000) private String symptoms;
    @Column(length=2000) private String medicalHistory;
    private String preferredDate;
    private String preferredTime;
    private String doctorName;
    private String specialist;
    private String confirmedDate;
    private String confirmedTime;
    @Enumerated(EnumType.STRING) private AppointmentStatus status;
    @Column(length=3000) private String hospitalNotes;
    private String createdAt;
    @PrePersist void created(){ createdAt=LocalDateTime.now().toString(); if(status==null) status=AppointmentStatus.PENDING; }
}
