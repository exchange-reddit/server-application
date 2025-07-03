package com.omniversity.server.user.AllUser;

import com.omniversity.server.user.entity.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbstractUserRepository extends JpaRepository<AbstractUser, Long> {
    Optional<AbstractUser> findByPrivateEmail(String privateEmail);
    Optional<AbstractUser> findByHomeEmail(String homeEmail);
    Optional<AbstractUser> findById(long id);
    Optional<AbstractUser> findByUserName(String userName);
    Boolean deleteById(long id);
}
