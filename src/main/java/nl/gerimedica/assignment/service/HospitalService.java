package nl.gerimedica.assignment.service;

import nl.gerimedica.assignment.domain.Appointment;
import nl.gerimedica.assignment.domain.Patient;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HospitalService {
    public List<Appointment> bulkCreateAppointments(
            String patientName,
            String ssn,
            List<String> reasons,
            List<String> dates
    );
    Patient findPatientBySSN(String ssn);
    void savePatient(Patient patient);
    List<Appointment> getAppointmentsByReason(String reasonKeyword);
    void deleteAppointmentsBySSN(String ssn);
    Appointment findLatestAppointmentBySSN(String ssn);
}
