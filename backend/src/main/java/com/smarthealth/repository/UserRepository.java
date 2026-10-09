package com.smarthealth.repository;
import com.smarthealth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
