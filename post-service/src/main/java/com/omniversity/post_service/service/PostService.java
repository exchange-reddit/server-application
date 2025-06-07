package com.omniversity.post_service.service;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.exception.PostNotFoundException;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public PostResponseDto createPost(PostCreateDto createDto) {
        Post post = postMapper.toEntity(createDto);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponseDto(savedPost);
    }

    public PostResponseDto updatePost(Long postId, PostUpdateDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        postMapper.updateEntity(post, updateDto);
        Post updatedPost = postRepository.save(post);
        return postMapper.toResponseDto(updatedPost);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
