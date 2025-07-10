package com.omniversity.verification_service.token;

import com.omniversity.verification_service.token.entity.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

    /**
     * About: Find token by the token value
     * Params: String value of the token
     * Returns: Registration token
     */
    Optional<RegistrationToken> findByCode(String code);

    /**
     * About: Find token by the email value of the user
     * Params: Email address of the verification token
     * Returns: Registration token
     */
    Optional<RegistrationToken> findByEmail(String email);

    /**
     * About: Find token by the combination of email and token value
     * Params: Email address, and token value of the verification token
     * Returns: Registration token
     */
    Optional<RegistrationToken> findByCodeAndEmail(String code, String email);

    /**
     * About: Delete registration token by the token value
     * Params: String value of the token
     */
    Long removeByCode(String code);
}

