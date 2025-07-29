package com.omniversity.post_service.service;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.exception.custom.InvalidInputException;
import com.omniversity.post_service.exception.custom.PostNotFoundException;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.repository.PostRepository;
import com.omniversity.post_service.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
    private final StorageService storageService;

    @Autowired
    private IMqttClient mqttClient;

    private static final String NEW_POST_TOPIC = "new-post";

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

    public PostResponseDto createPost(PostCreateDto createDto, MultipartFile file) throws InvalidInputException {
        // .toEntity() throws InvalidInputException when there are empty fields
        Post post = postMapper.toEntity(createDto);
        // Store file if present
        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file); // <-- Your abstraction for file storage
            post.setAttachmentPath(filePath); // You need a field for this in your Post entity
        }

        Post savedPost = postRepository.save(post);

        // Publish New Post Message to MQTT Broker
        String payload = String.format(
                "{\"postId\": %d, \"sectionId\": %d}",
                savedPost.getId(),
                createDto.getSectionId()
        );

        MqttMessage mqttMessage = new MqttMessage(payload.getBytes());
        mqttMessage.setQos(1);

        try {
            if (mqttClient.isConnected()) {
                mqttClient.publish(NEW_POST_TOPIC, mqttMessage);
                System.out.println("Published message to " + NEW_POST_TOPIC);
            } else {
                System.err.println("MQTT Client is not connected. Message not published");
            }
        } catch (MqttException e) {
            System.err.println("Error publishing MQTT message: " + e.getMessage());
            e.printStackTrace();
        }

        return postMapper.toResponseDto(savedPost);
    }

    public PostResponseDto updatePost(Long postId, PostUpdateDto updateDto, MultipartFile file) throws PostNotFoundException {
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
