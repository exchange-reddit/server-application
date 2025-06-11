package com.omniversity.post_service.service;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.exception.PostNotFoundException;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toResponseDto)
                .toList();
    }

    public List<PostListItemDto> getAllPostListItems() {
        return postRepository.findAllProjected();
    }

    public PostResponseDto getPostById(Long id) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        System.out.println("Post: " + post);
        PostResponseDto dto = postMapper.toResponseDto(post);
        dto.setEdited(!post.getCreatedAt().equals(post.getUpdatedAt()));
        System.out.println("DTO: " + dto);
        return dto;
    }

    public PostResponseDto createPost(PostCreateDto createDto) {
        Post post = postMapper.toEntity(createDto);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponseDto(savedPost);
    }

    public PostResponseDto updatePost(Long postId, PostUpdateDto updateDto) throws PostNotFoundException {
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
