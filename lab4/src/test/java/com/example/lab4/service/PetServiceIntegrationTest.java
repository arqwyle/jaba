package com.example.lab4.service;

import com.example.lab4.model.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class PetServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private PetService petService;

    @Test
    void testAddAndGetPet() {
        Pet pet = new Pet();
        pet.setName("aboba");

        Pet savedPet = petService.addPet(pet);
        Pet retrievedPet = petService.getPetById(savedPet.getId());

        assertNotNull(retrievedPet);
        assertEquals("aboba", retrievedPet.getName());
    }

    @Test
    void testUpdatePet() {
        Pet pet = new Pet();
        pet.setName("aboba");
        Pet savedPet = petService.addPet(pet);

        savedPet.setName("bebra");
        Pet updatedPet = petService.updatePet(savedPet);

        assertEquals("bebra", updatedPet.getName());
    }

    @Test
    void testDeletePet() {
        Pet pet = new Pet();
        pet.setName("aboba");
        Pet savedPet = petService.addPet(pet);

        petService.deletePet(savedPet.getId());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> petService.getPetById(savedPet.getId()));
        assertEquals("Pet not found", exception.getMessage());
    }
}
