package com.omniversity.public_community_service.PostDependency;

import org.aspectj.asm.internal.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDependencyRepository extends JpaRepository<PostDependencyEntity, Long> {
    List<Long> findAllPosts(Long sectionId);
    List<Long> findRecentPostsCustom(Long sectionId);

}
