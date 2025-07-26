package com.healthcare.appointment.service;

import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.entity.Patient;

import java.util.List;

public interface HospitalService {
    List<Appointment> bulkCreateAppointments(
            String patientName,
            String ssn,
            List<String> reasons,
            List<String> dates
    );

    Patient findPatientBySSN(String ssn);

    List<Appointment> getAppointmentsByReason(String reasonKeyword);

    void deleteAppointmentsBySSN(String ssn);

    Appointment findLatestAppointmentBySSN(String ssn);
}
