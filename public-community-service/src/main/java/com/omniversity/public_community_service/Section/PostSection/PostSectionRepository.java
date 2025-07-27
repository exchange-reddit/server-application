package com.omniversity.public_community_service.Section.PostSection;

import com.omniversity.public_community_service.Section.Entity.PostSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostSectionRepository extends JpaRepository<PostSection, Long> {
    Optional<PostSection> findById(Long id);
    @Query("SELECT p.id FROM POST_SECTION p WHERE p.community.id = :universityCommunityId")
    Optional<List<Long>> findByCommunityId(@Param("universityCommunityId") Long universityCommunityId);
}
