package com.omniversity.server.user.ProspectiveUser;

import com.omniversity.server.user.entity.ProspectiveUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProspectiveUserRepository extends JpaRepository<ProspectiveUser, Long> {
    Optional<ProspectiveUser> findByUserName(String userName);
    Optional<ProspectiveUser> findByPrivateEmail(String email);
    Optional<ProspectiveUser> findByHomeEmail(String email);
}