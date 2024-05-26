package dev.patika.VeterinaryManagementSystem.service;

import dev.patika.VeterinaryManagementSystem.dto.request.DoctorRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AvailableDateResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.DoctorResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.DoctorWithAvailableDateResponse;
import dev.patika.VeterinaryManagementSystem.entities.AvailableDate;
import dev.patika.VeterinaryManagementSystem.entities.Doctor;
import dev.patika.VeterinaryManagementSystem.mapper.AvailableDateMapper;
import dev.patika.VeterinaryManagementSystem.mapper.DoctorMapper;
import dev.patika.VeterinaryManagementSystem.repository.AvailableDateRepository;
import dev.patika.VeterinaryManagementSystem.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final AvailableDateService availableDateService;
    private final AvailableDateMapper availableDateMapper;
    private final AvailableDateRepository availableDateRepository;

    public List<DoctorResponse> findAll() {

        return doctorMapper.asOutput(doctorRepository.findAll());
    }

    public DoctorResponse getById(Long id) {
        return doctorMapper.asOutput(doctorRepository.findById(id).orElseThrow(()
                -> new RuntimeException(id + "Doctor with ID Couldn't Be Found!!!")));
    }

    // Evaluation Form 12
    // Building the necessary controller and service layers for doctor registration operations.
    public DoctorResponse create(DoctorRequest request) {
        Optional<Doctor> isDoctorExist = doctorRepository.findByName(request.getName());

        if (isDoctorExist.isEmpty()) {
            Doctor doctorSaved = doctorRepository.save(doctorMapper.asEntity(request));
            return doctorMapper.asOutput(doctorSaved);
        }
        throw new RuntimeException("This doctor has been registered in the system before!!!");
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Optional<Doctor> doctorFromDb = doctorRepository.findById(id);
        if (doctorFromDb.isEmpty()) {
            throw new RuntimeException(id + "The doctor you are trying to update could not be found in the system!!!");
        }

        Doctor doctor = doctorFromDb.get();
        doctorMapper.update(doctor, request);
        return doctorMapper.asOutput(doctorRepository.save(doctor));
    }

    public void deleteById(Long id) {
        Optional<Doctor> doctorFromDb = doctorRepository.findById(id);

        if (doctorFromDb.isPresent()) {
            doctorRepository.delete(doctorFromDb.get());
        } else {
            throw new RuntimeException(id + " Doctor no. could not be found.");
        }
    }

    public DoctorWithAvailableDateResponse getDoctorWithAvailableDates(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException(doctorId + " Doctor no. could not be found."));

        List<AvailableDate> availableDates = availableDateRepository.findByDoctor(doctor);

        List<AvailableDateResponse> availableDateResponses = availableDateMapper.asOutput(availableDates);

        return new DoctorWithAvailableDateResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getPhone(),
                doctor.getMail(),
                doctor.getAddress(),
                doctor.getCity(),
                availableDateResponses
        );
    }

}