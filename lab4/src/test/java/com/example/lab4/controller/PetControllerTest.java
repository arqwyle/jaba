package com.example.lab4.controller;

import com.example.lab4.model.Pet;
import com.example.lab4.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    void testAddPet() throws Exception {
        Pet pet = new Pet();
        pet.setName("aboba");

        when(petService.addPet(any())).thenReturn(pet);

        mockMvc.perform(post("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("aboba"));

        verify(petService).addPet(any());
    }

    @Test
    void testUpdatePet() throws Exception {
        Pet existingPet = new Pet();
        existingPet.setId(1L);
        existingPet.setName("aboba");

        when(petService.updatePet(any())).thenAnswer(invocation -> {
            Pet updatedPet = invocation.getArgument(0);
            updatedPet.setId(2L);
            updatedPet.setName("bebra");
            return updatedPet;
        });

        mockMvc.perform(put("/api/v3/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingPet)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("bebra"))
                .andExpect(jsonPath("$.id").value(2L));

        verify(petService).updatePet(any());
    }

    @Test
    void testGetPetById() throws Exception {
        Pet pet = new Pet();
        pet.setId(1L);

        when(petService.getPetById(1L)).thenReturn(pet);

        mockMvc.perform(get("/api/v3/pet/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

        verify(petService).getPetById(1L);
    }

    @Test
    void testDeletePet_Success() throws Exception {
        when(petService.deletePet(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v3/pet/1"))
                .andExpect(status().isNoContent());

        verify(petService).deletePet(1L);
    }
}