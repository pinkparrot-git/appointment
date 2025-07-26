package com.healthcare.appointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Appointment extends BaseEntity {
    private String reason;
    private String date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    public Appointment() {
    }

    public Appointment(String reason, String date, Patient patient) {
        this.reason = reason;
        this.date = date;
        this.patient = patient;
    }

}
