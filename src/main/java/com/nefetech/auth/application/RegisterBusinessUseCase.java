package com.nefetech.auth.application;

import com.nefetech.auth.application.ports.PasswordHasherPort;
import com.nefetech.auth.application.ports.TenantRepository;
import com.nefetech.auth.application.ports.TokenGenerator;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.domain.Tenant;
import com.nefetech.auth.domain.User;

import java.util.UUID;

public class RegisterBusinessUseCase {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordHasherPort passwordHasher;

    public RegisterBusinessUseCase(TenantRepository tenantRepository, UserRepository userRepository, TokenGenerator tokenGenerator, PasswordHasherPort passwordHasher) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordHasher = passwordHasher;
    }

    public String register(String businessName, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya se encuentra registrado");
        }

        // 1. Crear Tenant
        String tenantId = UUID.randomUUID().toString();
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.setBusinessName(businessName);
        tenant.setDatabaseName("db_" + tenantId.replace("-", "").substring(0, 10)); // Genera un sufijo corto aleatorio
        tenant.setStatus("ACTIVE");
        tenantRepository.save(tenant);

        // 2. Crear Usuario Dueño
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(passwordHasher.hash(rawPassword)); // Encriptado!
        user.setRole("ROLE_ADMIN");
        user.setStatus("ACTIVE");
        user.setTenantId(tenantId);
        userRepository.save(user);

        // 3. Devolver JWT
        return tokenGenerator.generateToken(user);
    }
}
