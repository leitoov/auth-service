package com.nefetech.auth.infrastructure.persistence.repository;

import com.nefetech.auth.infrastructure.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTenantRepository extends JpaRepository<TenantEntity, String> {
}
