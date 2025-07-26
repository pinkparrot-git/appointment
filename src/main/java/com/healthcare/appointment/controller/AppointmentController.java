package com.healthcare.appointment.controller;

import com.healthcare.appointment.Utility.HospitalUtils;
import com.healthcare.appointment.domain.AppointmentRequestDto;
import com.healthcare.appointment.domain.AppointmentResponseDto;
import com.healthcare.appointment.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
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
                                    schema = @Schema(implementation = AppointmentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body")
            }
    )
    @PostMapping("/bulk")
    public ResponseEntity<List<AppointmentResponseDto>> createBulkAppointments(
            @RequestBody @Valid AppointmentRequestDto requestDto
    ) {

        HospitalUtils.recordUsage("Bulk appointments creation triggered");
        List<AppointmentResponseDto> createdAppointments = hospitalService.bulkCreateAppointments(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointments);
    }

    @Operation(
            summary = "Get appointments by reason",
            description = "Fetches appointments filtered by a reason keyword. If no reason is provided, returns all.",
            responses = @ApiResponse(responseCode = "200", description = "List of appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentResponseDto.class)))
    )
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByReason(
            @RequestParam String reason,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        List<AppointmentResponseDto> appointments = hospitalService.getAppointmentsByReason(reason, page, size);
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
    public ResponseEntity<String> deleteAppointmentsBySSN(@RequestParam @NotEmpty String ssn) {
        hospitalService.deleteAppointmentsBySSN(ssn);
        return new ResponseEntity<>("Deleted all appointments for SSN: " + ssn, HttpStatus.OK);
    }


    @Operation(
            summary = "Get latest appointment by SSN",
            description = "Retrieves the most recent appointment for a patient based on SSN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Latest appointment found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppointmentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "No appointment found for given SSN")
            }
    )
    @GetMapping("/latest")
    public ResponseEntity<AppointmentResponseDto> getLatestAppointment(@RequestParam @NotEmpty String ssn) {
        AppointmentResponseDto latest = hospitalService.findLatestAppointmentBySSN(ssn);
        return new ResponseEntity<>(latest, HttpStatus.OK);
    }
}
