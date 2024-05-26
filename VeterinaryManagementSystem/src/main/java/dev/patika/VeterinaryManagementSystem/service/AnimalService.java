package dev.patika.VeterinaryManagementSystem.service;

import dev.patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.AnimalWithVaccineResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.VaccineResponse;
import dev.patika.VeterinaryManagementSystem.entities.Animal;
import dev.patika.VeterinaryManagementSystem.entities.Customer;
import dev.patika.VeterinaryManagementSystem.entities.Vaccine;
import dev.patika.VeterinaryManagementSystem.mapper.AnimalMapper;
import dev.patika.VeterinaryManagementSystem.mapper.VaccineMapper;
import dev.patika.VeterinaryManagementSystem.repository.AnimalRepository;
import dev.patika.VeterinaryManagementSystem.repository.CustomerRepository;
import dev.patika.VeterinaryManagementSystem.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final CustomerRepository customerRepository;
    private final VaccineMapper vaccinationMapper;
    private final VaccineRepository vaccineRepository;

    public List<AnimalResponse> findAll() {
        return animalMapper.asOutput(animalRepository.findAll());
    }

    public AnimalResponse getById(Long id) {
        return animalMapper.asOutput(animalRepository.findById(id).orElseThrow(()
                -> new RuntimeException(id + "id li Hayvan Bulunamadı !!!")));
    }

    // Evaluation Form 11
    // Building the controller and service layers that are required for operations involving animal recording
    public AnimalResponse createWithCustomer(Long customerId, AnimalRequest animalRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found."));

        Animal animal = animalMapper.asEntity(animalRequest);
        animal.setCustomer(customer);

        Animal savedAnimal = animalRepository.save(animal);
        return animalMapper.asOutput(savedAnimal);
    }

    // Evaluation Form 20
    // Building the service layers and controllers required to list the vaccinations for a particular animal
    public AnimalWithVaccineResponse getAnimalWithVaccines(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException(animalId + " Animal no. was not found."));

        List<Vaccine> vaccines = vaccineRepository.findByAnimalId(animal.getId());

        List<VaccineResponse> vaccineResponses = vaccinationMapper.asOutput(vaccines);

        return new AnimalWithVaccineResponse(
                animal.getId(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getGender(),
                animal.getColour(),
                animal.getDateOfBirth(),
                vaccineResponses
        );
    }

    public List<VaccineResponse> getVaccinationsByAnimalId(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException(animalId + " Animal no. was not found."));

        List<Vaccine> vaccinations = animal.getVaccines();
        return vaccinations.stream()
                .map(vaccinationMapper::asOutput)
                .collect(Collectors.toList());
    }

    // Evaluation Form 16
    // Building the controller and service layers that are required to filter animals by name
    public List<AnimalResponse> getAnimalsByName(String name) {
        List<Animal> animals = animalRepository.findByNameContainingIgnoreCase(name);
        return animals.stream()
                .map(animalMapper::asOutput)
                .collect(Collectors.toList());
    }

    public AnimalResponse update(Long id, AnimalRequest request) {
        Optional<Animal> animalFromDb = animalRepository.findById(id);
        Optional<Animal> isAnimalExist = animalRepository.findByName(request.getName());

        if (animalFromDb.isEmpty()) {
            throw new RuntimeException(id + "The animal you are trying to update was not found in the system!!!");
        }

        if (isAnimalExist.isPresent()) {
            throw new RuntimeException("This animal has been registered in the system before!!!");
        }
        Animal animal = animalFromDb.get();
        animalMapper.update(animal, request);
        return animalMapper.asOutput(animalRepository.save(animal));
    }

    public void deleteById(Long id) {
        Optional<Animal> animalFromDb = animalRepository.findById(id);

        if (animalFromDb.isPresent()) {
            animalRepository.delete(animalFromDb.get());
        } else {
            throw new RuntimeException(id + " Animal no. was not found.");
        }
    }

}