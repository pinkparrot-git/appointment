package com.healthcare.appointment.service.impl;

import com.healthcare.appointment.Utility.HospitalUtils;
import com.healthcare.appointment.domain.AppointmentRequestDto;
import com.healthcare.appointment.domain.AppointmentResponseDto;
import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.entity.Patient;
import com.healthcare.appointment.exception.ResourceNotFoundException;
import com.healthcare.appointment.mapper.AppointmentMapper;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.appointment.repository.PatientRepository;
import com.healthcare.appointment.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    @Transactional
    public List<AppointmentResponseDto> bulkCreateAppointments(AppointmentRequestDto requestDto) {
        Patient patient = patientRepo.getBySsn(requestDto.getSsn());
        if (patient == null) {
            log.info("Creating new patient with SSN: {}", requestDto.getSsn());
            patient = new Patient(requestDto.getPatientName(), requestDto.getSsn());
            patientRepo.save(patient);
        } else {
            log.info("Existing patient found, SSN: {}", patient.ssn);
        }

        List<Appointment> createdAppointments = new ArrayList<>();
        int loopSize = Math.min(requestDto.getReasons().size(), requestDto.getDates().size());

        for (int i = 0; i < loopSize; i++) {
            String reason = requestDto.getReasons().get(i);
            String date = requestDto.getDates().get(i).format(DATE_FORMATTER);
            Appointment appointment = new Appointment(reason, date, patient);
            createdAppointments.add(appointment);
        }
        appointmentRepo.saveAll(createdAppointments);

        for (Appointment appt : createdAppointments) {
            log.info("Created appointment for reason: {} [Date: {}] [Patient SSN: {}]", appt.reason, appt.date, appt.patient.ssn);
        }

        HospitalUtils.recordUsage("Bulk create appointments");
        return AppointmentMapper.toDtoList(createdAppointments);

    }

    public List<AppointmentResponseDto> getAppointmentsByReason(String reason, int page, int size) {
        Page<Appointment> appointments = appointmentRepo.findByReasonContainingIgnoreCase(
                reason,
                PageRequest.of(page, size)
        );
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found for reason: " + reason);
        }
        HospitalUtils.recordUsage("Get appointments by reason");
        return AppointmentMapper.toDtoList(appointments.getContent());
    }

    @Override
    @Transactional
    public void deleteAppointmentsBySSN(String ssn) {
        Patient patient = patientRepo.findBySsn(ssn)
                .orElseThrow(() -> new ResourceNotFoundException("No appointments found for patient with SSN: " + ssn));
        List<Appointment> appointments = patient.appointments;
        appointmentRepo.deleteAll(appointments);

    }

    @Override
    public AppointmentResponseDto findLatestAppointmentBySSN(String ssn) {
        Appointment latestAppointment = appointmentRepo.findFirstByPatientSsnOrderByDateDesc(ssn)
                .orElseThrow(() -> new ResourceNotFoundException("No appointments found for patient with SSN: " + ssn));
        return AppointmentMapper.toDto(latestAppointment);
    }
}
