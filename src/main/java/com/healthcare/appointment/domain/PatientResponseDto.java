package com.healthcare.appointment.domain;


import lombok.Data;

import java.util.List;

@Data
public class PatientResponseDto {
    private String name;
    private String ssn;
    private List<AppointmentResponseDto> appointments;
}
