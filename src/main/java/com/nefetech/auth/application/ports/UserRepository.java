package com.nefetech.auth.application.ports;

import com.nefetech.auth.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
