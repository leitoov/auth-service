package com.nefetech.auth.infrastructure.persistence.repository;

import com.nefetech.auth.infrastructure.persistence.entity.LoginLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataMongoAuditRepository extends MongoRepository<LoginLogDocument, String> {
}
