package nl.gerimedica.assignment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.gerimedica.assignment.domain.Appointment;
import nl.gerimedica.assignment.domain.Patient;
import nl.gerimedica.assignment.mapper.AppointmentMapper;
import nl.gerimedica.assignment.model.AppointmentRequestDTO;
import nl.gerimedica.assignment.model.AppointmentResponseDTO;
import nl.gerimedica.assignment.repository.AppointmentRepository;
import nl.gerimedica.assignment.repository.PatientRepository;
import nl.gerimedica.assignment.service.HospitalService;
import nl.gerimedica.assignment.utils.HospitalUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Transactional
    public List<AppointmentResponseDTO> createBulkAppointments(AppointmentRequestDTO request) {
        Patient found = findPatientBySSN(request.getSsn());
        if (found == null) {
            log.info("Creating new patient with SSN: {}", request.getSsn());
            found = new Patient(request.getPatientName(), request.getSsn());
            patientRepo.save(found);
        } else {
            log.info("Existing patient found, SSN: {}", found.ssn);
        }

        List<Appointment> createdAppointments = new ArrayList<>();
        int loopSize = Math.min(request.getReasons().size(), request.getDates().size());
        for (int i = 0; i < loopSize; i++) {
            String reason = request.getReasons().get(i);
            String date =  request.getDates().get(i).format(DATE_FORMATTER);
            Appointment appt = new Appointment(reason, date, found);
            createdAppointments.add(appt);
        }

        appointmentRepo.saveAll(createdAppointments);

        for (Appointment appt : createdAppointments) {
            log.info("Created appointment for reason: {} [Date: {}] [Patient SSN: {}]", appt.reason, appt.date, appt.patient.ssn);
        }

        HospitalUtils.recordUsage("Bulk create appointments");

        return AppointmentMapper.toDtoList(createdAppointments);
    }

    private Patient findPatientBySSN(String ssn) {
        List<Patient> all = patientRepo.findAll();
        for (Patient p : all) {
            if (p.ssn.equals(ssn)) {
                return p;
            }
        }
        return null;
    }

    public List<AppointmentResponseDTO> getAppointmentsByReason(String reasonKeyword) {
        List<Appointment> allAppointments = appointmentRepo.findAll();
        List<Appointment> matched = new ArrayList<>();

        for (Appointment ap : allAppointments) {
            if (ap.reason.contains(reasonKeyword)) {
                matched.add(ap);
            }
        }

        List<Appointment> finalList = new ArrayList<>();
        for (Appointment ap : matched) {
            if (ap.reason.equalsIgnoreCase(reasonKeyword)) {
                finalList.add(ap);
            }
        }

        HospitalUtils.recordUsage("Get appointments by reason");

        return AppointmentMapper.toDtoList(finalList);
    }

    public void deleteAppointmentsBySSN(String ssn) {
        Patient patient = findPatientBySSN(ssn);
        if (patient == null) {
            return;
        }
        List<Appointment> appointments = patient.appointments;
        appointmentRepo.deleteAll(appointments);
    }

    public AppointmentResponseDTO findLatestAppointmentBySSN(String ssn) {
        Patient patient = findPatientBySSN(ssn);
        if (patient == null || patient.appointments == null || patient.appointments.isEmpty()) {
            return null;
        }

        Appointment latest = null;
        for (Appointment appt : patient.appointments) {
            if (latest == null) {
                latest = appt;
            } else {
                if (appt.date.compareTo(latest.date) > 0) {
                    latest = appt;
                }
            }
        }

        return AppointmentMapper.toDto(latest);
    }
}
