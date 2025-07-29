package com.omniversity.public_community_service.SectionPostDependency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostSectionDependencyRepository extends JpaRepository<PostSectionDependencyEntity, Long> {
    @Query("SELECT psd.postId FROM PostSectionDependencyEntity psd WHERE psd.sectionId = :sectionId")
    List<Long> findAllPostIds(@Param("sectionId") Long sectionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostSectionDependencyEntity psd WHERE psd.postId = :postId")
    void deleteRemovedPosts(@Param("postId") Long postId);

}
