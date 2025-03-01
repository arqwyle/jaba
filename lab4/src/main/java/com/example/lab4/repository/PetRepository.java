package com.example.lab4.repository;

import com.example.lab4.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PetRepository {

    private final Map<Long, Pet> petDatabase = new HashMap<>();
    private Long currentId = 1L;

    public Pet addPet(Pet pet) {
        if (pet.getId() == null) {
            pet.setId(currentId++);
        }
        petDatabase.put(pet.getId(), pet);
        return pet;
    }

    public Pet updatePet(Pet pet) {
        if (petDatabase.containsKey(pet.getId())) {
            petDatabase.put(pet.getId(), pet);
            return pet;
        }
        return null;
    }

    public Pet getPetById(Long petId) {
        return petDatabase.get(petId);
    }

    public boolean deletePet(Long petId) {
        return petDatabase.remove(petId) != null;
    }

    public List<Pet> getAllPets() {
        return new ArrayList<>(petDatabase.values());
    }
}
