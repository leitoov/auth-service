package com.nefetech.auth.application.ports;

public interface AuditPort {
    void logLoginAttempt(String email, String status, String ipAddress, String deviceType);
}
