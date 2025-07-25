package com.omniversity.public_community_service.Section.PostSection;

import com.omniversity.public_community_service.Section.Entity.PostSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostSectionRepository extends JpaRepository<PostSection, Long> {
    Optional<PostSection> findById(Long id);
}
