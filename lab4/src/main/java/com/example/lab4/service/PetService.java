package com.example.lab4.service;

import com.example.lab4.model.Pet;
import com.example.lab4.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet addPet(Pet pet) {
        validatePet(pet);
        return petRepository.addPet(pet);
    }

    public Pet updatePet(Pet pet) {
        validatePet(pet);
        if (petRepository.getPetById(pet.getId()) == null) {
            throw new RuntimeException("Pet not found");
        }
        return petRepository.updatePet(pet);
    }

    public Pet getPetById(Long petId) {
        Pet pet = petRepository.getPetById(petId);
        if (pet == null) {
            throw new RuntimeException("Pet not found");
        }
        return pet;
    }

    public boolean deletePet(Long petId) {
        if (petRepository.getPetById(petId) != null) {
            return petRepository.deletePet(petId);
        }
        return false;
    }

    public List<Pet> getAllPets() {
        return petRepository.getAllPets();
    }

    private void validatePet(Pet pet) {
        if (pet == null || pet.getName() == null || pet.getName().isEmpty()) {
            throw new RuntimeException("Invalid pet data");
        }
    }
}
