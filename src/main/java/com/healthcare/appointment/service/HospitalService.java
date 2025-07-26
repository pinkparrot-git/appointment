package com.healthcare.appointment.service;

import com.healthcare.appointment.domain.AppointmentRequestDto;
import com.healthcare.appointment.domain.AppointmentResponseDto;

import java.util.List;

public interface HospitalService {
    List<AppointmentResponseDto> bulkCreateAppointments(AppointmentRequestDto requestDto);

    List<AppointmentResponseDto> getAppointmentsByReason(String reason, int page, int size);

    void deleteAppointmentsBySSN(String ssn);

    AppointmentResponseDto findLatestAppointmentBySSN(String ssn);
}
