package com.smarthealth.repository;
import com.smarthealth.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByIdDesc(Long patientId); List<Appointment> findAllByOrderByIdDesc();
}
