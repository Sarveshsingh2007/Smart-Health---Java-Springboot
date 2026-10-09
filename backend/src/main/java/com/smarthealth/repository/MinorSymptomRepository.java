package com.smarthealth.repository;
import com.smarthealth.model.MinorSymptom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MinorSymptomRepository extends JpaRepository<MinorSymptom, Long> {
    List<MinorSymptom> findAllByOrderByNameAsc();
}
