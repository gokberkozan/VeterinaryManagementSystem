package dev.patika.VeterinaryManagementSystem.controller;

import dev.patika.VeterinaryManagementSystem.dto.request.AnimalRequest;
import dev.patika.VeterinaryManagementSystem.dto.request.VaccineRequest;
import dev.patika.VeterinaryManagementSystem.dto.response.AnimalResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.CustomerResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.DoctorResponse;
import dev.patika.VeterinaryManagementSystem.dto.response.VaccineResponse;
import dev.patika.VeterinaryManagementSystem.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vaccine")
public class VaccineController {
    private final VaccineService vaccineService;

    @Autowired
    public VaccineController(VaccineService vaccineService){
        this.vaccineService = vaccineService;
    }

    @GetMapping()
    public List<VaccineResponse> findAll() {
        return vaccineService.findAll();
    }

    @GetMapping("/{id}")
    public void getById(@PathVariable Long id) {
        vaccineService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        vaccineService.deleteById(id);
    }

    @PutMapping ("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VaccineResponse update(@PathVariable Long id, @RequestBody VaccineRequest request) {
        return vaccineService.update(id, request);
    }

    // Evaluation Form 15
    // Building the controller and service layers required to keep track of the animal's immunization
    @PostMapping("/create-with-animal/{animalId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VaccineResponse saveWithAnimal(
            @PathVariable Long animalId,
            @RequestBody VaccineRequest vaccineRequest
    ) {
        return vaccineService.createWithAnimal(animalId, vaccineRequest);
    }

    // Evaluation Form 21
    // Building the required controller and service layers to provide an animal's immunization history sorted by date range
    @GetMapping("/findByDateRange")
    public List<VaccineResponse> findByProtectionStartDateBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return vaccineService.findByProtectionStartDateBetween(startDate, endDate);
    }

}