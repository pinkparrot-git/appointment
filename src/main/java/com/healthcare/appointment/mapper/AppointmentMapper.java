package com.healthcare.appointment.mapper;



import com.healthcare.appointment.domain.AppointmentResponseDto;
import com.healthcare.appointment.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static AppointmentResponseDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setReason(appointment.getReason());

        dto.setDate(parseLocalDate(appointment.getDate()));

        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        return dto;
    }

    public static List<AppointmentResponseDto> toDtoList(List<Appointment> appointments) {
        return appointments.stream().map(AppointmentMapper::toDto).collect(Collectors.toList());
    }

    private static LocalDate parseLocalDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }}
