package com.main.patientservice.service;

import billing.BillingServiceGrpc;
import com.main.patientservice.dto.PatientRequestDTO;
import com.main.patientservice.dto.PatientResponseDTO;
import com.main.patientservice.exception.EmailAlreadyExistsException;
import com.main.patientservice.exception.PatientNotFoundException;
import com.main.patientservice.grpc.BillingServiceGrpcClient;
import com.main.patientservice.kafka.kafkaProducer;
import com.main.patientservice.mapper.PatientMapper;
import com.main.patientservice.model.Patient;
import com.main.patientservice.repository.PatientRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Business logic is in service class ex- email should be unique
@Service
public class PatientService {
//    Injecting dependency(di) using constructor injection
    private final kafkaProducer kafkaProducer;
    private PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    // constructor injection
    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, kafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients (){
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(PatientMapper::toDTO).toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient (PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
          throw  new EmailAlreadyExistsException("A patient with this email "
          + "already exists" + patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));

//        Whenever a new patient created and saved create a new billing account for that
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(),newPatient.getEmail());

//      This is of/for kafka event
        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient (PatientRequestDTO patientRequestDTO, UUID id){

        Patient patient = patientRepository.findById(id).orElseThrow(
                ()->new PatientNotFoundException("Patient with id {} not exist"+ id));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){
            throw new EmailAlreadyExistsException("A patient with this email "
                    + "already exists" + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));


        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(@PathVariable UUID id){
        patientRepository.deleteById(id);
    }

}
