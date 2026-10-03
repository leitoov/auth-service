package com.nefetech.auth.domain;

import lombok.Data;

@Data
public class User {
    private String id;
    private String email;
    private String password;
    private String role;
    private String status;
    private String tenantId;
}
