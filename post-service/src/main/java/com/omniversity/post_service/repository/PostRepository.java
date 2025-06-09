package com.omniversity.post_service.repository;

import java.util.List;

import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new com.omniversity.post_service.dto.PostListItemDto(p.id, p.title, p.createdAt, p.authorId, p.status) FROM Post p")
    List<PostListItemDto> findAllProjected();
}
