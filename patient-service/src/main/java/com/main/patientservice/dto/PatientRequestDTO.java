package com.main.patientservice.dto;

import com.main.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatientRequestDTO {
//    Another reason of using DTOs is to use validation
    @NotBlank(message = "Name is required")
    @Size(max=100, message="Name can't exceed 100 character")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "DOB is required")
    private String dateOfBirth;

    @NotNull(groups = CreatePatientValidationGroup.class,
            message = "Registered date is required")
    private String registeredDate;

    public @NotBlank(message = "Name is required") @Size(max = 100, message = "Name can't have more than 100 character") String getName() {
        return name;
    }

    public void setName(
            @NotBlank(message = "Name is required") @Size(max = 100, message = "Name can't exceed more  than 100 character")String name) {
        this.name = name;
    }

    public @NotBlank(message = "Email is required") @Size(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(
            @NotBlank(message = "Email is required") @Size(message = "Email should be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(
            @NotBlank(message = "Address is required") String address) {
        this.address = address;
    }

    public @NotBlank(message = "DOB is required") String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(
            @NotBlank(message = "DOB is required") String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
