package nl.gerimedica.assignment.model;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentRequestDTO {
    @NotNull
    private String patientName;
    @NotNull
    private String ssn;
    @NotNull
    private List<String> reasons;
    @NotNull
    private List<LocalDate> dates;
}
