package com.example.lab8;

import com.example.lab8.model.User;
import com.example.lab8.repository.UserRepository;
import com.example.lab8.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    User createUser(String username, String authority) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setAuthority(authority);
        return userService.createUser(user);
    }

    @Test
    void getAllUsersAsAdmin() throws Exception {
        createUser("admin", "ADMIN");

        Map<String, String> loginRequest = Map.of(
                "username", "admin",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String adminToken = loginResult.getResponse().getContentAsString();

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.username == 'admin')]").exists());;
    }

    @Test
    void getAllUsersAsUser() throws Exception {
        createUser("user", "USER");

        Map<String, String> loginRequest = Map.of(
                "username", "user",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String userToken = loginResult.getResponse().getContentAsString();

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.username == 'user')]").exists());
    }

    @Test
    void createUserAsAdmin() throws Exception {
        createUser("admin", "ADMIN");

        Map<String, String> loginRequest = Map.of(
                "username", "admin",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String adminToken = loginResult.getResponse().getContentAsString();

        User newUser = createUser("newUser", "USER");

        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUser"));
    }

    @Test
    void createUserAsUser() throws Exception {
        createUser("user", "USER");

        Map<String, String> loginRequest = Map.of(
                "username", "user",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String userToken = loginResult.getResponse().getContentAsString();

        User newUser = createUser("newUser", "USER");

        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserAsAdmin() throws Exception {
        createUser("admin", "ADMIN");
        User userToUpdate = createUser("oldUser", "USER");

        Map<String, String> loginRequest = Map.of(
                "username", "admin",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String adminToken = loginResult.getResponse().getContentAsString();

        userToUpdate.setUsername("updatedUser");

        mockMvc.perform(put("/users/" + userToUpdate.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    void updateUserAsUser() throws Exception {
        createUser("user", "USER");
        User userToUpdate = createUser("oldUser", "USER");

        Map<String, String> loginRequest = Map.of(
                "username", "user",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String userToken = loginResult.getResponse().getContentAsString();

        userToUpdate.setUsername("updatedUser");

        mockMvc.perform(put("/users/" + userToUpdate.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserAsAdmin() throws Exception {
        createUser("admin", "ADMIN");
        User userToDelete = createUser("userToDelete", "USER");

        Map<String, String> loginRequest = Map.of(
                "username", "admin",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String adminToken = loginResult.getResponse().getContentAsString();

        mockMvc.perform(delete("/users/" + userToDelete.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserAsUser() throws Exception {
        createUser("user", "USER");
        User userToDelete = createUser("userToDelete", "USER");

        Map<String, String> loginRequest = Map.of(
                "username", "user",
                "password", "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String userToken = loginResult.getResponse().getContentAsString();

        mockMvc.perform(delete("/users/" + userToDelete.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
