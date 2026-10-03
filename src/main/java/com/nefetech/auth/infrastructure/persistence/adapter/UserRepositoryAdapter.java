package com.nefetech.auth.infrastructure.persistence.adapter;

import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.domain.User;
import com.nefetech.auth.infrastructure.persistence.entity.UserEntity;
import com.nefetech.auth.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpaRepository;

    public UserRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(entity -> {
            User user = new User();
            user.setId(entity.getId());
            user.setEmail(entity.getEmail());
            user.setPassword(entity.getPassword());
            user.setRole(entity.getRole());
            user.setStatus(entity.getStatus());
            user.setTenantId(entity.getTenantId());
            return user;
        });
    }
}
