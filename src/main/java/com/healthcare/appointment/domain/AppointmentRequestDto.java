package com.healthcare.appointment.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AppointmentRequestDto {
    @NotBlank(message = "Patient name must not be blank")
    private String patientName;
    @NotBlank(message = "SSN must not be blank")
    private String ssn;

    @NotNull(message = "Reasons list cannot be null")
    private List<String> reasons = new ArrayList<>();

    @NotNull(message = "Dates list cannot be null")
    private List<LocalDate> dates;
}