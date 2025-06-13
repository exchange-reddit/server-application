package com.omniversity.server.user;

import com.omniversity.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Check if the transferred dto is a duplicate or not (privateEmail, exchangeEmail, homeEmail)
    User findByPrivateEmail(String privateEmail);
    User findByExchangeEmail(String exchangeEmail);
    User findByHomeEmail(String homeEmail);
    User findById(long userId);
    User findByUserId(String userId);
    Boolean deleteById(long userId);

}
