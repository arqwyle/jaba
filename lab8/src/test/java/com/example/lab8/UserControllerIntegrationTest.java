package com.example.lab8;

import com.example.lab8.model.User;
import com.example.lab8.repository.UserRepository;
import com.example.lab8.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
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
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    User createUser(String username, String authority) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setAuthority(authority);
        return userService.createUser(user);
    }

    String loginAndGetToken(String username) {
        String loginUrl = "http://localhost:" + port + "/auth/login";
        Map<String, String> loginRequest = Map.of(
                "username", username,
                "password", "password"
        );
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, loginRequest, String.class);
        return response.getBody();
    }

    @Test
    void getAllUsersAsAdmin() {
        createUser("admin", "ADMIN");
        String adminToken = loginAndGetToken("admin");

        String url = "http://localhost:" + port + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<User[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(Arrays.stream(response.getBody()).anyMatch(u -> u.getUsername().equals("admin")));
    }

    @Test
    void getAllUsersAsUser() {
        createUser("user", "USER");
        String userToken = loginAndGetToken("user");

        String url = "http://localhost:" + port + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);

        ResponseEntity<User[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(Arrays.stream(response.getBody()).anyMatch(u -> u.getUsername().equals("user")));
    }

    @Test
    void createUserAsAdmin() {
        createUser("admin", "ADMIN");
        String adminToken = loginAndGetToken("admin");

        String url = "http://localhost:" + port + "/users";
        User newUser = createUser("newUser", "USER");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<User> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(newUser, headers),
                User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newUser", response.getBody().getUsername());
    }

    @Test
    void createUserAsUser() {
        createUser("user", "USER");
        String userToken = loginAndGetToken("user");

        String url = "http://localhost:" + port + "/users";
        User newUser = createUser("newUser", "USER");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(newUser, headers),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateUserAsAdmin() {
        createUser("admin", "ADMIN");
        User userToUpdate = createUser("oldUser", "USER");
        String adminToken = loginAndGetToken("admin");

        String url = "http://localhost:" + port + "/users/" + userToUpdate.getId();
        userToUpdate.setUsername("updatedUser");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<User> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(userToUpdate, headers),
                User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updatedUser", response.getBody().getUsername());
    }

    @Test
    void updateUserAsUser() {
        createUser("user", "USER");
        User userToUpdate = createUser("oldUser", "USER");
        String userToken = loginAndGetToken("user");

        String url = "http://localhost:" + port + "/users/" + userToUpdate.getId();
        userToUpdate.setUsername("updatedUser");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(userToUpdate, headers),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deleteUserAsAdmin() {
        createUser("admin", "ADMIN");
        User userToDelete = createUser("userToDelete", "USER");
        String adminToken = loginAndGetToken("admin");

        String url = "http://localhost:" + port + "/users/" + userToDelete.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUserAsUser() {
        createUser("user", "USER");
        User userToDelete = createUser("userToDelete", "USER");
        String userToken = loginAndGetToken("user");

        String url = "http://localhost:" + port + "/users/" + userToDelete.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
