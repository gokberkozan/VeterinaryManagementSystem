package dev.patika.VeterinaryManagementSystem.controller;

import dev.patika.VeterinaryManagementSystem.dto.request.AppointmentDateRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AppointmentDateResponse;
import dev.patika.VeterinaryManagementSystem.service.AppointmentDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointmentDate")
public class AppointmentDateController {
    private final AppointmentDateService appointmentDateService;

    @Autowired
    public AppointmentDateController(AppointmentDateService appointmentDateService) {
        this.appointmentDateService = appointmentDateService;
    }

    // Evaluation Form 14
    // Setting up the service layers and controller needed to preserve the appointment date
    // Evaluation Form 22
    // A new appointment cannot be recorded for a date that is not available or if the doctor is scheduled for another appointment during that time.
    @PostMapping("/create-with-doctor-and-animal/{doctorId}/{animalId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDateResponse saveWithDoctorAndAnimal(
            @PathVariable Long doctorId,
            @PathVariable Long animalId,
            @RequestBody AppointmentDateRequest appointmentDateRequest) {
        return appointmentDateService.createWithDoctorAndAnimal(doctorId, animalId, appointmentDateRequest);
    }

    // Evaluation Form 24
    // Building the required controller and service layers to filter appointments based on the doctor and date range
    @GetMapping("/filter-by-date-range-and-doctor")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentDateResponse> getAppointmentsByDateRangeAndDoctor(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long doctorId) {
        return appointmentDateService.getAppointmentsByDateRangeAndDoctorId(startDate, endDate, doctorId);
    }

    // Evaluation Form 23
    // Building the required controller and service layers to filter appointments based on the animal and date range
    @GetMapping("/filter-by-date-range-and-animal")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentDateResponse> getAppointmentsByDateRangeAndAnimal(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long animalId) {
        return appointmentDateService.getAppointmentsByDateRangeAndAnimalId(startDate, endDate, animalId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentDateResponse> findAll() {
        return appointmentDateService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentDateResponse getById(Long id) {
        return appointmentDateService.getById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentDateResponse update(@PathVariable Long id, @RequestBody AppointmentDateRequest request) {
        return appointmentDateService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appointmentDateService.deleteById(id);
    }

}