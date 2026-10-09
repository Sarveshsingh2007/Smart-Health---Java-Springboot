package com.smarthealth.controller;
import com.smarthealth.model.*;
import com.smarthealth.repository.*;
import com.smarthealth.service.ExcelService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@RestController @RequestMapping("/api/admin")
public class AdminController {
 @Autowired UserRepository users; @Autowired AppointmentRepository apps; @Autowired BillRepository bills; @Autowired ExcelService excel;
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){
   return Map.of("patients",users.findAll().stream().filter(u->u.getRole()==Role.PATIENT).toList(),
     "hospitals",users.findAll().stream().filter(u->u.getRole()==Role.HOSPITAL).toList(),
     "appointments",apps.findAllByOrderByIdDesc(),"bills",bills.findAllByOrderByIdDesc());
 }
 @GetMapping("/export/{type}") public ResponseEntity<byte[]> export(@PathVariable String type)throws Exception{
   byte[] data; String name;
   switch(type.toLowerCase()){
     case "appointments" -> {data=excel.appointments(apps.findAllByOrderByIdDesc());name="appointments.xlsx";}
     case "bills" -> {data=excel.bills(bills.findAllByOrderByIdDesc());name="bills.xlsx";}
     case "patients" -> {data=excel.patients(users.findAll());name="patients.xlsx";}
     default -> throw new IllegalArgumentException("Unknown export type");
   }
   return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+name)
     .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(data);
 }
}
