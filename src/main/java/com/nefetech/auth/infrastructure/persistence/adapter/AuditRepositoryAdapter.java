package com.nefetech.auth.infrastructure.persistence.adapter;

import com.nefetech.auth.application.ports.AuditPort;
import com.nefetech.auth.infrastructure.persistence.entity.LoginLogDocument;
import com.nefetech.auth.infrastructure.persistence.repository.SpringDataMongoAuditRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuditRepositoryAdapter implements AuditPort {

    private final SpringDataMongoAuditRepository mongoRepository;

    public AuditRepositoryAdapter(SpringDataMongoAuditRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Async
    @Override
    public void logLoginAttempt(String email, String status, String ipAddress, String deviceType) {
        LoginLogDocument log = new LoginLogDocument();
        log.setEmail(email);
        log.setStatus(status);
        log.setIpAddress(ipAddress);
        log.setDeviceType(deviceType);
        log.setTimestamp(new Date());
        
        mongoRepository.save(log);
    }
}
