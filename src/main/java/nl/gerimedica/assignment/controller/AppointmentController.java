package nl.gerimedica.assignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.gerimedica.assignment.domain.Appointment;
import nl.gerimedica.assignment.model.AppointmentRequestDTO;
import nl.gerimedica.assignment.service.HospitalService;
import nl.gerimedica.assignment.utils.HospitalUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@Tag(name = "Appointment Management", description = "Endpoints for managing hospital appointments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AppointmentController {
    private final HospitalService hospitalService;

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
            @Valid @RequestBody AppointmentRequestDTO request) {

        HospitalUtils.recordUsage("Bulk appointments creation triggered");

        List<Appointment> createdAppointments = hospitalService.bulkCreateAppointments(request);

        return ResponseEntity
                .created(URI.create("/api/v1/appointments"))
                .body(createdAppointments);
    }

    @Operation(
            summary = "Get appointments by reason",
            description = "Fetches appointments filtered by a reason keyword. If no reason is provided, returns all.",
            responses = @ApiResponse(responseCode = "200", description = "List of appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appointment.class)))
    )
    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointmentsByReason(
            @RequestParam(required = false) String reason) {

        List<Appointment> appointments = hospitalService.getAppointmentsByReason(reason);
        return ResponseEntity.ok(appointments);
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
    public ResponseEntity<Void> deleteAppointmentsBySSN(@RequestParam String ssn) {
        hospitalService.deleteAppointmentsBySSN(ssn);
        return ResponseEntity.noContent().build();
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
        return latest != null ? ResponseEntity.ok(latest) : ResponseEntity.notFound().build();
    }
}
