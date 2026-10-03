package com.nefetech.auth.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "login_logs")
public class LoginLogDocument {

    @Id
    private String id;
    
    private String email;
    private Date timestamp;
    private String status;
    private String ipAddress;
    private String deviceType;
}
