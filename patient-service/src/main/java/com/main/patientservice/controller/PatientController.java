package com.main.patientservice.controller;

import com.main.patientservice.dto.PatientRequestDTO;
import com.main.patientservice.dto.PatientResponseDTO;
import com.main.patientservice.dto.validators.CreatePatientValidationGroup;
import com.main.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient",description = "API for managing Patients")
public class PatientController {
    private final PatientService patientService;

//    Here using constructor injection, can use @Autowired or Beans also.
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients = patientService.getPatients();
//        ResponseEntity take up the DTO and convert it to JSON
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    @Operation(summary="Create a new Patients")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary="Update a Patients")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
           @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(patientRequestDTO, id);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Delete a Patients")
    public ResponseEntity<Void>deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
