package com.healthcare.appointment;

import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.entity.Patient;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.appointment.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppointmentApplicationTests {
	private RestTemplate restTemplate;

	@Autowired
	private AppointmentRepository appointmentRepo;

	@Autowired
	private PatientRepository patientRepo;

    @LocalServerPort
    private int port;

	@BeforeEach
	void setUp() {
		restTemplate = new RestTemplate();

		Patient patient = new Patient("Test Patient", "123456789");
		patient = patientRepo.save(patient);

		Appointment appointment = new Appointment("Checkup", "2025-07-26", patient);
		appointmentRepo.save(appointment);
	}
	@Test
	void testSuccess() {
        String url = "http://localhost:" + port + "/api/v1/appointment?keyword=Checkup";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String body = response.getBody();
        assertNotNull(body);

		assertTrue(body.contains("\"reason\":\"Checkup\""));
	}
}
