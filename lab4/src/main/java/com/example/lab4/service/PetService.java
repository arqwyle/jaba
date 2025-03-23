package com.example.lab4.service;

import com.example.lab4.model.Pet;
import com.example.lab4.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet addPet(Pet pet) {
        validatePet(pet);
        return petRepository.save(pet); // Метод save() сохраняет или обновляет сущность
    }

    public Pet updatePet(Pet pet) {
        validatePet(pet);
        Optional<Pet> existingPet = petRepository.findById(pet.getId());
        if (existingPet.isEmpty()) {
            throw new RuntimeException("Pet not found");
        }
        return petRepository.save(pet);
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    public boolean deletePet(Long petId) {
        if (!petRepository.existsById(petId)) {
            return false;
        }
        petRepository.deleteById(petId);
        return true;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    private void validatePet(Pet pet) {
        if (pet == null || pet.getName() == null || pet.getName().isEmpty()) {
            throw new RuntimeException("Invalid pet data");
        }
    }
}
