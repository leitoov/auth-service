package com.nefetech.auth.application.ports;

import com.nefetech.auth.domain.Tenant;

public interface TenantRepository {
    Tenant save(Tenant tenant);
}
