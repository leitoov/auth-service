package com.nefetech.auth.application;

import com.nefetech.auth.application.ports.PasswordHasherPort;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.domain.User;

import java.util.UUID;

public class RegisterEmployeeUseCase {

    private final UserRepository userRepository;
    private final PasswordHasherPort passwordHasher;

    public RegisterEmployeeUseCase(UserRepository userRepository, PasswordHasherPort passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public void register(String email, String password, String role, String tenantId) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya se encuentra registrado en el sistema");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(passwordHasher.hash(password)); // Encriptado
        user.setRole(role);
        user.setStatus("ACTIVE");
        user.setTenantId(tenantId); // ¡Se ancla automáticamente al tenant del jefe!

        userRepository.save(user);
    }
}
