package com.omniversity.server.user.ExchangeUser;

import com.omniversity.server.user.entity.ExchangeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeUserRepository extends JpaRepository<ExchangeUser, Long> {
    Optional<ExchangeUser> findByPrivateEmail(String privateEmail);
    Optional<ExchangeUser> findByExchangeEmail(String exchangeEmail);
    Optional<ExchangeUser> findByHomeEmail(String exchangeEmail);
    Optional<ExchangeUser> findById(long userId);
    Optional<ExchangeUser> findByUserName(String userName);
}
