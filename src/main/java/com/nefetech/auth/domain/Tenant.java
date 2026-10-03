package com.nefetech.auth.domain;

import lombok.Data;

@Data
public class Tenant {
    private String id;
    private String businessName;
    private String databaseName;
    private String status;
}
