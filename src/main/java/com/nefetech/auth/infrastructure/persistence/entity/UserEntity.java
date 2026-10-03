package com.nefetech.auth.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    
    @Id
    private String id;
    
    private String email;
    private String password;
    private String role;
    private String status;
    private String tenantId;
}
