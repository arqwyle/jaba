package org.example.lab10.controller;

import jakarta.annotation.security.RolesAllowed;
import org.example.lab10.model.UserRequestDTO;
import org.example.lab10.service.KeyCloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private KeyCloakService keyCloakService;

    @GetMapping
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        List<UserRepresentation> users = keyCloakService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        keyCloakService.addUser(userRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody UserRequestDTO userRequestDTO) {
        keyCloakService.updateUser(userId, userRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        keyCloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
