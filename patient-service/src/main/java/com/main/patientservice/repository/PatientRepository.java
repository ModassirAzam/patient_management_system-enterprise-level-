package com.main.patientservice.repository;

import com.main.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
//  JPA checks in back if email is already exist or not
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
