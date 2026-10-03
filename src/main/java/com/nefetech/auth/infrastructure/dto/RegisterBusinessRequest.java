package com.nefetech.auth.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterBusinessRequest {
    
    @NotBlank(message = "El nombre del negocio es obligatorio")
    private String businessName;
    
    @NotBlank(message = "El email del dueño es obligatorio")
    @Email(message = "Formato de email inválido")
    private String ownerEmail;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String ownerPassword;
}
