package com.smarthealth.config;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(Exception.class)
 public ResponseEntity<Map<String,String>> handle(Exception e){
   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()==null?"Request failed":e.getMessage()));
 }
}
