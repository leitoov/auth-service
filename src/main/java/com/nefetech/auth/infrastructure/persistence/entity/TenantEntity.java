package com.nefetech.auth.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tenants")
public class TenantEntity {
    @Id
    private String id;
    private String businessName;
    private String databaseName;
    private String status;
}
