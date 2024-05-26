package dev.patika.VeterinaryManagementSystem.service;

import dev.patika.VeterinaryManagementSystem.dto.request.AvailableDateRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AvailableDateResponse;
import dev.patika.VeterinaryManagementSystem.entities.AppointmentDate;
import dev.patika.VeterinaryManagementSystem.entities.AvailableDate;
import dev.patika.VeterinaryManagementSystem.entities.Doctor;
import dev.patika.VeterinaryManagementSystem.mapper.AvailableDateMapper;
import dev.patika.VeterinaryManagementSystem.repository.AvailableDateRepository;
import dev.patika.VeterinaryManagementSystem.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailableDateService {

    private final AvailableDateRepository availableDateRepository;
    private final AvailableDateMapper availableDateMapper;
    private final DoctorRepository doctorRepository;

    public List<AvailableDateResponse> findAll() {
        return availableDateMapper.asOutput(availableDateRepository.findAll());
    }

    public AvailableDateResponse findById(Long id) {
        return availableDateMapper.asOutput(availableDateRepository.findById(id).orElseThrow(()
                -> new RuntimeException(id + "No available day with id found!!!")));
    }

    // Evaluation Form 13
    // Building the required service tiers and controller to record the doctor's available day
    public AvailableDateResponse createWithDoctor(Long doctorId, AvailableDateRequest request) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("The doctor could not be found."));

        AvailableDate availableDate = availableDateMapper.asEntity(request);
        availableDate.setDoctor(doctor);

        AvailableDate savedAvailableDate = availableDateRepository.save(availableDate);
        return availableDateMapper.asOutput(savedAvailableDate);

    }

    public AvailableDateResponse update(Long id, AvailableDateRequest request) {
        AvailableDate existingAvailableDate = availableDateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + "No available day with id found!!!"));

        // Use AvailableDateMapper to update the entity if one is necessary.
        availableDateMapper.update(existingAvailableDate, request);

        // Updated entities should be saved to databases.
        AvailableDate updatedAvailableDate = availableDateRepository.save(existingAvailableDate);

        return availableDateMapper.asOutput(updatedAvailableDate);
    }

    public void deleteById(Long id) {
        // Delete the relevant available day from the database
        availableDateRepository.deleteById(id);
    }

}