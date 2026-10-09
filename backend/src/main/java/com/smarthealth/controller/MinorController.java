package com.smarthealth.controller;
import com.smarthealth.model.MinorSymptom;
import com.smarthealth.repository.MinorSymptomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/minor")
public class MinorController {
    private final MinorSymptomRepository repo;
    public MinorController(MinorSymptomRepository repo){this.repo=repo;}
    @GetMapping("/{id}")
    public ResponseEntity<MinorSymptom> getSymptomById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/symptoms") public List<MinorSymptom> all(){return repo.findAllByOrderByNameAsc();}
    @GetMapping("/symptoms/{id}") public MinorSymptom one(@PathVariable Long id){return repo.findById(id).orElseThrow();}
}
