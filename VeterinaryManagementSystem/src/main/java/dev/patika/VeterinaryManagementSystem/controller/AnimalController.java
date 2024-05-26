package dev.patika.VeterinaryManagementSystem.controller;

import dev.patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.AnimalWithVaccineResponse;
import dev.patika.VeterinaryManagementSystem.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/animal")
public class AnimalController {

    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AnimalResponse> findAll() {
        return animalService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnimalResponse getById(@PathVariable Long id) {
        return animalService.getById(id);
    }


    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnimalResponse update(@PathVariable Long id, @RequestBody AnimalRequest request) {
        return animalService.update(id, request);
    }

    @DeleteMapping("/delete/{animalId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("animalId") Long animalId) {
        animalService.deleteById(animalId);
    }

    // Evaluation Form 11
    // Building the controller and service layers that are required for operations involving animal recording
    @PostMapping("/create-with-customer/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse saveWithCustomer(
            @PathVariable Long customerId,
            @RequestBody AnimalRequest animalRequest
    ) {
        return animalService.createWithCustomer(customerId, animalRequest);
    }

    // Evaluation Form 16
    // Building the controller and service layers that are required to filter animals by name
    @GetMapping("/filter-by-name")
    @ResponseStatus(HttpStatus.OK)
    public List<AnimalResponse> getAnimalsByName(@RequestParam String name) {
        return animalService.getAnimalsByName(name);
    }

    // Evaluation Form 20
    // Building the service layers and controllers required to list the vaccinations for a particular animal
    @GetMapping("{id}/with-vaccines")
    @ResponseStatus(HttpStatus.OK)
    public AnimalWithVaccineResponse getAnimalWithVaccines(@PathVariable Long id) {
        return animalService.getAnimalWithVaccines(id);
    }

}