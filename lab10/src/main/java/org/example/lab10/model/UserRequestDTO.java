package org.example.lab10.model;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String role;
}
