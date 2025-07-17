package nl.gerimedica.assignment.service;

import nl.gerimedica.assignment.domain.Appointment;
import nl.gerimedica.assignment.domain.Patient;
import nl.gerimedica.assignment.model.AppointmentRequestDTO;

import java.util.List;

public interface HospitalService {
    public List<Appointment> bulkCreateAppointments(AppointmentRequestDTO request);

    Patient findPatientBySSN(String ssn);

    void savePatient(Patient patient);

    List<Appointment> getAppointmentsByReason(String reasonKeyword);

    void deleteAppointmentsBySSN(String ssn);

    Appointment findLatestAppointmentBySSN(String ssn);
}
