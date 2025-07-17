package nl.gerimedica.assignment.service;

import nl.gerimedica.assignment.domain.Appointment;
import nl.gerimedica.assignment.domain.Patient;
import nl.gerimedica.assignment.model.AppointmentRequestDTO;
import nl.gerimedica.assignment.model.AppointmentResponseDTO;

import java.util.List;

public interface HospitalService {
    public List<AppointmentResponseDTO> createBulkAppointments(AppointmentRequestDTO request);
    List<AppointmentResponseDTO> getAppointmentsByReason(String reasonKeyword);

    void deleteAppointmentsBySSN(String ssn);

    AppointmentResponseDTO findLatestAppointmentBySSN(String ssn);
}
