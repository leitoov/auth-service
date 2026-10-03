package com.nefetech.auth.infrastructure.persistence.adapter;

import com.nefetech.auth.application.ports.TenantRepository;
import com.nefetech.auth.domain.Tenant;
import com.nefetech.auth.infrastructure.persistence.entity.TenantEntity;
import com.nefetech.auth.infrastructure.persistence.repository.SpringDataTenantRepository;
import org.springframework.stereotype.Component;

@Component
public class TenantRepositoryAdapter implements TenantRepository {

    private final SpringDataTenantRepository repository;

    public TenantRepositoryAdapter(SpringDataTenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tenant save(Tenant tenant) {
        TenantEntity entity = new TenantEntity();
        entity.setId(tenant.getId());
        entity.setBusinessName(tenant.getBusinessName());
        entity.setDatabaseName(tenant.getDatabaseName());
        entity.setStatus(tenant.getStatus());
        
        entity = repository.save(entity);
        return tenant;
    }
}
