package dev.patika.VeterinaryManagementSystem.service;

import dev.patika.VeterinaryManagementSystem.dto.request.VaccineRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.VaccineResponse;
import dev.patika.VeterinaryManagementSystem.entities.Animal;
import dev.patika.VeterinaryManagementSystem.entities.Vaccine;
import dev.patika.VeterinaryManagementSystem.mapper.VaccineMapper;
import dev.patika.VeterinaryManagementSystem.repository.AnimalRepository;
import dev.patika.VeterinaryManagementSystem.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper;
    private final AnimalService animalService;
    private final AnimalRepository animalRepository;

    public List<VaccineResponse> findAll() {
       return vaccineMapper.asOutput(vaccineRepository.findAll());

    }

    // Evaluation Form 21
    // Building the required controller and service layers to provide an animal's immunization history sorted by date range
    public List<VaccineResponse> findByProtectionStartDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Vaccine> vaccines = vaccineRepository.findByProtectionStartDateBetween(startDate, endDate);
        return vaccineMapper.asOutput(vaccines);
    }

    public Vaccine findById (Long id) {
        return vaccineRepository.findById(id).orElseThrow(() ->
                new RuntimeException(id + "Vaccine with ID Couldn't Be Found!!!"));
    }

    public void deleteById(Long id) {
        Optional<Vaccine> vaccineFromDb = vaccineRepository.findById(id);
        if (vaccineFromDb.isPresent()) {
            vaccineRepository.delete(vaccineFromDb.get());
        } else {
            throw new RuntimeException(id + "The vaccine with ID was not found in the system!!!");
        }
    }

    // Evaluation Form #5
    // Building the service layers and controllers required to record the animal's vaccination
    // Evaluation Form 19
    // A new vaccination can be registered if the protection period has passed or if the vaccine with the same name has never been registered previously.
    public VaccineResponse createWithAnimal(Long animalId, VaccineRequest vaccineRequest) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException(animalId + "No animal with id was found."));

        Optional<Vaccine> existingVaccine = vaccineRepository.findByAnimalAndName(animal, vaccineRequest.getName());

        if (existingVaccine.isPresent() && existingVaccine.get().getProtectionFinishDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("A vaccine with this name is already registered and its protection date has not expired.");
        }

        if (existingVaccine.isEmpty() || existingVaccine.get().getProtectionFinishDate().isBefore(LocalDate.now())) {
            Vaccine vaccine = vaccineMapper.asEntity(vaccineRequest);
            vaccine.setAnimal(animal);
            Vaccine savedVaccine = vaccineRepository.save(vaccine);
            return vaccineMapper.asOutput(savedVaccine);
        } else {
            throw new RuntimeException("A vaccine with this name is already registered and its protection date has not expired.");
        }
    }

    public List<Vaccine> findByAnimalId(Long id) {
        return vaccineRepository.findByAnimalId(id);
    }

    public VaccineResponse update(Long id, VaccineRequest request) {
        Optional<Vaccine> vaccineFromDb = vaccineRepository.findById(id);
        Optional<Vaccine> isVaccineExist = vaccineRepository.findByCode(request.getCode());

        if (vaccineFromDb.isEmpty()) {
            throw new RuntimeException(id + "The vaccine you are trying to update was not found in the system!!!");
        }

        if (isVaccineExist.isPresent()) {
            throw new RuntimeException("This vaccine has already been registered in the system!!!");
        }

        Vaccine vaccine = vaccineFromDb.get();
        vaccineMapper.update(vaccine, request);
        return vaccineMapper.asOutput(vaccineRepository.save(vaccine));
    }

}