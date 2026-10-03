package com.nefetech.auth.application;

import com.nefetech.auth.domain.User;
import com.nefetech.auth.domain.Tenant;
import com.nefetech.auth.domain.exceptions.UserBlockedException;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.application.ports.TokenGenerator;
import com.nefetech.auth.application.ports.AuditPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private AuditPort auditPort;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    void whenUserIsBlocked_thenThrowUserBlockedException() {
        // Arrange (Preparación)
        String email = "juan@empresa.com";
        String password = "Password123!";
        
        User blockedUser = new User();
        blockedUser.setEmail(email);
        blockedUser.setPassword("hashedPassword");
        blockedUser.setStatus("BLOCKED");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(blockedUser));

        // Act & Assert (Ejecución y Verificación)
        assertThrows(UserBlockedException.class, () -> {
            loginUseCase.login(email, password, "desktop");
        });

        // Verificamos que no se intentó generar ningún token
        verify(tokenGenerator, never()).generateToken(any());
    }
}
