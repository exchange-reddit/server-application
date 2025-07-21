package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity;

import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityCommunityRepository extends JpaRepository<UniversityCommunity, Long> {
    Optional<UniversityCommunity> findById(Long id);
}
