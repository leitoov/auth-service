package com.nefetech.auth.infrastructure.controller;

import com.nefetech.auth.application.LoginUseCase;
import com.nefetech.auth.domain.exceptions.UserBlockedException;
import com.nefetech.auth.infrastructure.dto.LoginRequest;
import com.nefetech.auth.infrastructure.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Se delega al caso de uso, que está en la capa de Aplicación
            String jwt = loginUseCase.login(request.getEmail(), request.getPassword(), request.getDeviceType());
            
            // Retornar JWT si fue exitoso (86400000 ms = 24hs por ahora hardcodeado)
            return ResponseEntity.ok(new LoginResponse(jwt, 86400000));
            
        } catch (UserBlockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // Manejo genérico para bad credentials u otros errores
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas o error interno");
        }
    }
}
