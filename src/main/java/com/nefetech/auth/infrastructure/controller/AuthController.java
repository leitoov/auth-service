package com.nefetech.auth.infrastructure.controller;

import com.nefetech.auth.application.LoginUseCase;
import com.nefetech.auth.application.RegisterBusinessUseCase;
import com.nefetech.auth.application.RegisterEmployeeUseCase;
import com.nefetech.auth.application.ports.TokenValidatorPort;
import com.nefetech.auth.domain.exceptions.UserBlockedException;
import com.nefetech.auth.infrastructure.dto.LoginRequest;
import com.nefetech.auth.infrastructure.dto.LoginResponse;
import com.nefetech.auth.infrastructure.dto.RegisterBusinessRequest;
import com.nefetech.auth.infrastructure.dto.RegisterEmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints para inicio de sesión y registro de negocios/empleados")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final RegisterEmployeeUseCase registerEmployeeUseCase;
    private final TokenValidatorPort tokenValidator;

    public AuthController(LoginUseCase loginUseCase, RegisterBusinessUseCase registerBusinessUseCase, RegisterEmployeeUseCase registerEmployeeUseCase, TokenValidatorPort tokenValidator) {
        this.loginUseCase = loginUseCase;
        this.registerBusinessUseCase = registerBusinessUseCase;
        this.registerEmployeeUseCase = registerEmployeeUseCase;
        this.tokenValidator = tokenValidator;
    }

    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales del usuario y devuelve un token JWT.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String jwt = loginUseCase.login(request.getEmail(), request.getPassword(), request.getDeviceType());
            return ResponseEntity.ok(new LoginResponse(jwt, 86400000));
        } catch (UserBlockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas o error interno");
        }
    }

    @Operation(summary = "Registrar negocio", description = "Crea un nuevo negocio (Tenant) y su usuario administrador principal. Devuelve un JWT.")
    @PostMapping("/register-business")
    public ResponseEntity<?> registerBusiness(@Valid @RequestBody RegisterBusinessRequest request) {
        try {
            String jwt = registerBusinessUseCase.register(request.getBusinessName(), request.getOwnerEmail(), request.getOwnerPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(jwt, 86400000));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Registrar empleado", description = "Permite a un administrador registrar a un nuevo empleado dentro de su propio negocio.")
    @PostMapping("/register-employee")
    public ResponseEntity<?> registerEmployee(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody RegisterEmployeeRequest request) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token faltante o inválido");
            }
            
            String token = authHeader.substring(7);
            Map<String, Object> claims = tokenValidator.extractClaims(token);
            
            String role = (String) claims.get("role");
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden registrar empleados.");
            }
            
            String tenantId = (String) claims.get("tenantId");
            
            registerEmployeeUseCase.register(request.getEmail(), request.getPassword(), request.getRole(), tenantId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Empleado registrado con éxito");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar empleado: " + e.getMessage());
        }
    }
}
