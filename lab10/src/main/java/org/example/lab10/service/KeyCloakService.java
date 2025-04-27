package org.example.lab10.service;

import lombok.RequiredArgsConstructor;
import org.example.lab10.model.UserRequestDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyCloakService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public void addUser(UserRequestDTO dto) {
        String username = dto.getUsername();
        CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        UsersResource usersResource = getUsersResource();
        usersResource.create(user);
        addRealmRoleToUser(username, dto.getRole());
    }

    public List<UserRepresentation> getAllUsers() {
        return getUsersResource().list();
    }

    public void deleteUser(String userId) {
        getUsersResource().delete(userId);
    }

    public void updateUser(String userId, UserRequestDTO dto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.getUsername());
        user.setEnabled(true);

        if (dto.getPassword() != null) {
            CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
            user.setCredentials(Collections.singletonList(credential));
        }

        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        RoleMappingResource roleMappingResource = userResource.roles();

        List<RoleRepresentation> existingRoles = roleMappingResource.realmLevel().listAll();
        roleMappingResource.realmLevel().remove(existingRoles);

        RoleRepresentation role = realmResource.roles().get(dto.getRole()).toRepresentation();
        roleMappingResource.realmLevel().add(Collections.singletonList(role));

        userResource.update(user);
    }

    private void addRealmRoleToUser(String userName, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        List<UserRepresentation> users = realmResource.users().search(userName);
        UserResource userResource = realmResource.users().get(users.get(0).getId());
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        RoleMappingResource roleMappingResource = userResource.roles();
        roleMappingResource.realmLevel().add(Collections.singletonList(role));
    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }
}
