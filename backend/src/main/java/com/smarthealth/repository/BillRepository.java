package com.smarthealth.repository;
import com.smarthealth.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientIdOrderByIdDesc(Long patientId); List<Bill> findAllByOrderByIdDesc();
}
