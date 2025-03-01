package com.example.lab4.service;

import com.example.lab4.model.Pet;
import com.example.lab4.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPet() {
        Pet pet = new Pet();
        pet.setName("aboba");

        when(petRepository.addPet(any())).thenReturn(pet);

        Pet result = petService.addPet(pet);

        assertNotNull(result);
        assertEquals("aboba", result.getName());
    }

    @Test
    void testUpdatePet() {
        Pet existingPet = new Pet();
        existingPet.setId(1L);
        existingPet.setName("aboba");

        when(petRepository.getPetById(existingPet.getId())).thenReturn(existingPet);
        when(petRepository.updatePet(any())).thenAnswer(invocation -> {
            Pet updatedPet = invocation.getArgument(0);
            updatedPet.setId(2L);
            updatedPet.setName("bebra");
            return updatedPet;
        });

        Pet updatedPet = petService.updatePet(existingPet);

        assertNotNull(updatedPet);
        assertEquals("bebra", updatedPet.getName());
        assertEquals(2L, updatedPet.getId());
    }

    @Test
    void testGetPetById() {
        Pet pet = new Pet();
        pet.setId(1L);

        when(petRepository.getPetById(any())).thenReturn(pet);

        Pet result = petService.getPetById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeletePet() {
        Pet existingPet = new Pet();
        existingPet.setId(1L);

        when(petRepository.getPetById(1L)).thenReturn(existingPet);
        when(petRepository.deletePet(1L)).thenReturn(true);

        boolean result = petService.deletePet(1L);

        assertTrue(result);
    }
}