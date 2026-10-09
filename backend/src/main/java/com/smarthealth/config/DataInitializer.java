package com.smarthealth.config;
import com.smarthealth.model.*;
import com.smarthealth.repository.*;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {
 @Autowired MinorSymptomRepository symptoms; @Autowired MedicineRepository medicines; @Autowired UserRepository users; @Autowired PasswordEncoder encoder;
 @Override public void run(String... args){
   if(symptoms.count()==0){
     symptoms.saveAll(List.of(
       MinorSymptom.builder().name("Common Cold").description("Runny nose, mild cough and sore throat may occur.").medicines("Use only medicines recommended by a qualified clinician. Paracetamol may be considered for fever/pain when appropriate.").food("Warm fluids, soups, fruits and adequate hydration.").precautions("Rest, hand hygiene and avoid sharing utensils.").whenToDoctor("Seek care for breathing difficulty, persistent high fever, chest pain, dehydration or worsening symptoms.").build(),
       MinorSymptom.builder().name("Mild Headache").description("A mild headache can have many causes including stress, dehydration or lack of sleep.").medicines("Do not self-medicate repeatedly. Ask a pharmacist/doctor about an appropriate pain reliever.").food("Hydration and regular balanced meals.").precautions("Rest, sleep adequately and reduce screen strain.").whenToDoctor("Urgent care for sudden severe headache, weakness, confusion, fainting or vision changes.").build(),
       MinorSymptom.builder().name("Mild Stomach Upset").description("Temporary nausea, bloating or loose stool can occur.").medicines("Oral rehydration may help when there is fluid loss. Seek professional advice before other medicines.").food("Small bland meals and fluids.").precautions("Avoid unsafe food and maintain hygiene.").whenToDoctor("Doctor review for blood in stool/vomit, severe pain, dehydration or persistent symptoms.").build()
     ));
   }
   if(medicines.count()==0){
     medicines.saveAll(List.of(
       med("Common Cold","Paracetamol","For fever/pain only when clinically appropriate.",50),
       med("Common Cold","Cetirizine","For allergy-related symptoms; clinician/pharmacist advice recommended.",40),
       med("Mild Headache","Paracetamol","For pain when appropriate.",50),
       med("Mild Stomach Upset","ORS","Oral rehydration support.",25)
     ));
   }
 }
 private Medicine med(String d,String n,String desc,double p){return Medicine.builder().disease(d).name(n).description(desc).defaultPrice(p).build();}
}
