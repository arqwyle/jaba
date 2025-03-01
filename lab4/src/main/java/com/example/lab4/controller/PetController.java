package com.example.lab4.controller;

import com.example.lab4.model.Pet;
import com.example.lab4.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        try {
            Pet addedPet = petService.addPet(pet);
            return ResponseEntity.ok(addedPet);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) {
        try {
            Pet updatedPet = petService.updatePet(pet);
            return ResponseEntity.ok(updatedPet);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        try {
            Pet pet = petService.getPetById(petId);
            return ResponseEntity.ok(pet);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        if (petService.deletePet(petId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
