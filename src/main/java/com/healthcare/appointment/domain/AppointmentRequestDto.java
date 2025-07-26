package com.healthcare.appointment.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class AppointmentRequestDto {
    @NotBlank(message = "Patient name must not be blank")
    private String patientName;
    @NotBlank(message = "SSN must not be blank")
    private String ssn;

    @NotEmpty(message = "Reasons list cannot be null")
    private List<String> reasons;

    @NotEmpty(message = "Dates list cannot be null")
    private List<LocalDate> dates;
}