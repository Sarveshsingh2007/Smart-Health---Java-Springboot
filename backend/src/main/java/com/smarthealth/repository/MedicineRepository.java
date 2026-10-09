package com.smarthealth.repository;
import com.smarthealth.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByDiseaseIgnoreCase(String disease);
}
