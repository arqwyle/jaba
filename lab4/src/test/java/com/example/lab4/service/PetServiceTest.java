package com.example.lab4.service;

import com.example.lab4.model.Pet;
import com.example.lab4.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        pet.setId(1L);
        pet.setName("aboba");

        when(petRepository.save(pet)).thenReturn(pet);

        Pet result = petService.addPet(pet);

        assertNotNull(result);
        assertEquals("aboba", result.getName());
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void testUpdatePet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("bebra");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(pet)).thenReturn(pet);

        Pet result = petService.updatePet(pet);

        assertNotNull(result);
        assertEquals("bebra", result.getName());
        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void testUpdatePet_NotFound() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("aboba");

        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> petService.updatePet(pet));
        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPetById() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("aboba");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Pet result = petService.getPetById(1L);

        assertNotNull(result);
        assertEquals("aboba", result.getName());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPetById_NotFound() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> petService.getPetById(1L));
        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    void testDeletePet() {
        when(petRepository.existsById(1L)).thenReturn(true);

        petService.deletePet(1L);

        verify(petRepository, times(1)).existsById(1L);
        verify(petRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePet_NotFound() {
        when(petRepository.existsById(1L)).thenReturn(false);

        boolean result = petService.deletePet(1L);

        assertFalse(result);
        verify(petRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetAllPets() {
        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("aboba");

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("bebra");

        when(petRepository.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        List<Pet> result = petService.getAllPets();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("aboba", result.get(0).getName());
        assertEquals("bebra", result.get(1).getName());
        verify(petRepository, times(1)).findAll();
    }
}