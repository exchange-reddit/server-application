package com.omniversity.post_service.service;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.exception.PostNotFoundException;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.repository.PostRepository;
import com.omniversity.post_service.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    @Autowired
    private StorageService storageService;

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
        PostResponseDto dto = postMapper.toResponseDto(post);
        dto.setEdited(!post.getCreatedAt().equals(post.getUpdatedAt()));
        return dto;
    }

    public PostResponseDto createPost(PostCreateDto createDto, MultipartFile file) throws IOException {
        Post post = postMapper.toEntity(createDto);
        // Store file if present
        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file); // <-- Your abstraction for file storage
            post.setAttachmentPath(filePath); // You need a field for this in your Post entity
        }

        Post savedPost = postRepository.save(post);
        return postMapper.toResponseDto(savedPost);
    }

    public PostResponseDto updatePost(Long postId, PostUpdateDto updateDto, MultipartFile file) throws PostNotFoundException, IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (file != null && !file.isEmpty()) {
            if (post.getAttachmentPath() != null) {
                storageService.delete(post.getAttachmentPath());
            }
            String newFilePath = storageService.store(file);
            post.setAttachmentPath(newFilePath);
        }

        postMapper.updateEntity(post, updateDto);
        Post updatedPost = postRepository.save(post);
        return postMapper.toResponseDto(updatedPost);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
    }
}
