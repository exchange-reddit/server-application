package com.omniversity.server.user;

import com.omniversity.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Check if the transferred dto is a duplicate or not (privateEmail, exchangeEmail, homeEmail)
    Optional<User> findByPrivateEmail(String privateEmail);
    Optional<User> findByExchangeEmail(String exchangeEmail);
    Optional<User> findByHomeEmail(String homeEmail);
    Optional<User> findByUserId(String userId);
}
