package com.healthcare.appointment.controller;

import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.service.HospitalServiceImpl;
import com.healthcare.appointment.Utility.HospitalUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AppointmentController {

    private final HospitalServiceImpl hospitalService;

    /**
     * Example: {
     * "reasons": ["Checkup", "Follow-up", "X-Ray"],
     * "dates": ["2025-02-01", "2025-02-15", "2025-03-01"]
     * }
     */
    @Operation(
            summary = "Create multiple appointments",
            description = "Creates multiple appointments for a patient using reasons and dates",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Appointments created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Appointment.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body")
            }
    )
    @PostMapping("/bulk")
    public ResponseEntity<List<Appointment>> createBulkAppointments(
            @RequestParam String patientName,
            @RequestParam String ssn,
            @RequestBody Map<String, List<String>> payload
    ) {
        List<String> reasons = payload.get("reasons");
        List<String> dates = payload.get("dates");

        HospitalUtils.recordUsage("Bulk appointments creation triggered");
        List<Appointment> createdAppointments = hospitalService.bulkCreateAppointments(patientName, ssn, reasons, dates);
        return new ResponseEntity<>(createdAppointments, HttpStatus.OK);
    }

    @Operation(
            summary = "Get appointments by reason",
            description = "Fetches appointments filtered by a reason keyword. If no reason is provided, returns all.",
            responses = @ApiResponse(responseCode = "200", description = "List of appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appointment.class)))
    )
    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointmentsByReason(@RequestParam String keyword) {
        List<Appointment> appointments = hospitalService.getAppointmentsByReason(keyword);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete appointments by SSN",
            description = "Deletes all appointments associated with the given SSN",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Appointments deleted"),
                    @ApiResponse(responseCode = "404", description = "No appointments found for the SSN")
            }
    )
    @DeleteMapping
    public ResponseEntity<String> deleteAppointmentsBySSN(@RequestParam String ssn) {
        hospitalService.deleteAppointmentsBySSN(ssn);
        return new ResponseEntity<>("Deleted all appointments for SSN: " + ssn, HttpStatus.OK);
    }


    @Operation(
            summary = "Get latest appointment by SSN",
            description = "Retrieves the most recent appointment for a patient based on SSN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Latest appointment found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Appointment.class))),
                    @ApiResponse(responseCode = "404", description = "No appointment found for given SSN")
            }
    )
    @GetMapping("/latest")
    public ResponseEntity<Appointment> getLatestAppointment(@RequestParam String ssn) {
        Appointment latest = hospitalService.findLatestAppointmentBySSN(ssn);
        return new ResponseEntity<>(latest, HttpStatus.OK);
    }
}
