package com.omniversity.public_community_service.PublicCommunity.AbstractCommunity;

import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbstractCommunityRepository extends JpaRepository<AbstractCommunity, Long> {
    Optional<AbstractCommunity> findById(Long id);
    Optional<AbstractCommunity> findByName(String name);
}
