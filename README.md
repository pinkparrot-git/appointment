# Healthcare Appointment Application

Welcome! This is a Spring Boot project designed for managing healthcare appointments. It provides APIs to create and retrieve patient appointments efficiently.
---

## What to Expect

- A **simple** REST API for managing `Patients` and their `Appointments`.
- Multiple classes (controllers, services, entities, and repositories).
- **Incomplete** or **inefficient** approaches to certain tasks.

---

## Glossary

Below are the primary entities you’ll find in this codebase:

1. **Patient**
    - Represents an individual in the hospital system.
    - Fields may include:
        - `id`: auto-generated primary key
        - `name`: name of the patient
        - `ssn`: Social Security Number (used here as a unique identifier)
        - `appointments`: a list of `Appointment` objects linked to this patient

2. **Appointment**
    - Represents a scheduled appointment or event for a patient.
    - Fields may include:
        - `id`: auto-generated primary key
        - `reason`: a textual reason for the appointment (e.g., “Checkup”)
        - `date`: the date of the appointment
        - `patient`: a reference to the `Patient` who owns this appointment

---

## Goals

1. **Explore the codebase**: Familiarize yourself with the structure and logic.
2. **Identify potential issues**: Think about security, performance, maintainability, design patterns, etc.
3. **Propose and/or implement improvements**: Refactor, rewrite, or reorganize parts of the code to showcase your approach.

---
