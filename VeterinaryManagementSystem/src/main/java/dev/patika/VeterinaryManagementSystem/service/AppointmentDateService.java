package dev.patika.VeterinaryManagementSystem.service;

import dev.patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import dev.patika.VeterinaryManagementSystem.dto.request.AppointmentDateRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.AppointmentDateResponse;
import dev.patika.VeterinaryManagementSystem.entities.Animal;
import dev.patika.VeterinaryManagementSystem.entities.AppointmentDate;
import dev.patika.VeterinaryManagementSystem.entities.AvailableDate;
import dev.patika.VeterinaryManagementSystem.entities.Doctor;
import dev.patika.VeterinaryManagementSystem.mapper.AppointmentDateMapper;
import dev.patika.VeterinaryManagementSystem.repository.AnimalRepository;
import dev.patika.VeterinaryManagementSystem.repository.AppointmentDateRepository;
import dev.patika.VeterinaryManagementSystem.repository.AvailableDateRepository;
import dev.patika.VeterinaryManagementSystem.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentDateService {

    private final AppointmentDateRepository appointmentDateRepository;
    private final AppointmentDateMapper appointmentDateMapper;
    private final DoctorRepository doctorRepository;
    private final AnimalRepository animalRepository;
    private final AvailableDateRepository availableDateRepository;

    public List<AppointmentDateResponse> findAll() {
        return appointmentDateMapper.asOutput(appointmentDateRepository.findAll());
    }

    public AppointmentDateResponse getById(Long id) {
        return appointmentDateMapper.asOutput(appointmentDateRepository.findById(id).orElseThrow(()
                -> new RuntimeException(id + "Appointment with id not found!!!")));
    }

    // Evaluation Form 14
    // Building the service levels and controller needed to preserve the appointment date
    // Evaluation Form 22
    // A new appointment cannot be recorded for a date that is not available or if the doctor is scheduled for another appointment during that time.
    public AppointmentDateResponse createWithDoctorAndAnimal(Long doctorId, Long animalId, AppointmentDateRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("The doctor could not be found."));

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("The animal was not found."));

        LocalDateTime appointmentDate = request.getAppointmentDate();

        if (!isDoctorAvailable(doctorId, appointmentDate)) {
            throw new RuntimeException("The doctor's day is not available or he/she has another appointment.");
        }

        AppointmentDate appointment = appointmentDateMapper.asEntity(request);
        appointment.setDoctor(doctor);
        appointment.setAnimal(animal);
        AppointmentDate savedAppointment = appointmentDateRepository.save(appointment);
        return appointmentDateMapper.asOutput(savedAppointment);
    }

    public AppointmentDateResponse update(Long id, AppointmentDateRequest request) {
        AppointmentDate existingAppointment = appointmentDateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "Appointment with id not found!!!"));

        // Use AppointmentDateMapper to update the entity if one is needed.
        appointmentDateMapper.update(existingAppointment, request);

        // Updated entities should be saved to databases.
        AppointmentDate updatedAppointment = appointmentDateRepository.save(existingAppointment);

        return appointmentDateMapper.asOutput(updatedAppointment);
    }

    public void deleteById(Long id) {
        appointmentDateRepository.deleteById(id);
    }

    // Evaluation Form 24
    // Building the required controller and service layers to filter appointments based on the doctor and date range
    public List<AppointmentDateResponse> getAppointmentsByDateRangeAndDoctorId(LocalDate startDate, LocalDate endDate, Long doctorId) {
        List<AppointmentDate> filteredAppointments = appointmentDateRepository
                .findByAppointmentDateBetweenAndDoctorId(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), doctorId);

        return appointmentDateMapper.asOutput(filteredAppointments);
    }

    // Evaluation Form 23
    // Building the required controller and service layers to filter appointments based on the animal and date range
    public List<AppointmentDateResponse> getAppointmentsByDateRangeAndAnimalId(LocalDate startDate, LocalDate endDate, Long animalId) {
        List<AppointmentDate> filteredAppointments = appointmentDateRepository
                .findByAppointmentDateBetweenAndAnimalId(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), animalId);

        return appointmentDateMapper.asOutput(filteredAppointments);
    }

    // Evaluation Form 22
    // A new appointment cannot be recorded for a date that is not available or if the doctor is scheduled for another appointment during that time.
    public boolean isDoctorAvailable(Long doctorId, LocalDateTime appointmentDate) {
       // Checking whether the doctor is available that day
        List<AvailableDate> availableDates = availableDateRepository.findByDoctorIdAndAvailableDate(doctorId, appointmentDate.toLocalDate());

        if (availableDates.isEmpty()) {
            return false;
        }

        // If the doctor's day is available, checking whether he/she has another appointment at that time.
        List<AppointmentDate> doctorAppointments = appointmentDateRepository.findByDoctorId(doctorId);
        return doctorAppointments.stream()
                .noneMatch(appointment -> appointment.getAppointmentDate().equals(appointmentDate));
    }

}