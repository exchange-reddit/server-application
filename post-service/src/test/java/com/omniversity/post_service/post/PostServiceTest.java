package com.omniversity.post_service.post;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.entity.PostStatus;
import com.omniversity.post_service.exception.custom.InvalidInputException;
import com.omniversity.post_service.exception.custom.PostNotFoundException;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.repository.PostRepository;
import com.omniversity.post_service.service.PostService;
import com.omniversity.post_service.service.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setTitle("testTitle");
        post.setContent("testContent");
        post.setAuthorId(1L);
        post.setCommunityId(1L);
        post.setDeleted(false);
        post.setStatus(PostStatus.PUBLISHED);
        post.setAttachmentPath("testPath");
    }

    @Test
    void testGetAllPosts_Success() {
        when(postRepository.findAll()).thenReturn(List.of(post));

        List<PostResponseDto> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository, times(1)).findAll();
        verify(postMapper, times(1)).toResponseDto(any(Post.class));
    }

    @Test
    void testGetPostById_Success() {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(1L);
        dto.setCreatedAt(Instant.now());
        dto.setUpdatedAt(Instant.now());
        dto.setTitle("testTitle");
        dto.setContent("testContent");
        dto.setAuthorId(1L);
        dto.setCommunityId(1L);
        dto.setStatus(PostStatus.PUBLISHED);
        dto.setEdited(false);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toResponseDto(post)).thenReturn(dto);

        PostResponseDto actualDto = postService.getPostById(1L);

        assertEquals(dto, actualDto);
    }

    @Test
    void testGetPostById_Fail_PostNotFound() {
        when(postRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostById(2L));
    }

    @Test
    void testGetAllPostListItems_Success() {
        when(postRepository.findAllProjected()).thenReturn(List.of());

        postService.getAllPostListItems();

        verify(postRepository, times(1)).findAllProjected();
    }

    @Test
    void testCreatePost_Success_FileEmpty() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("testPost");
        dto.setContent("testContent");
        dto.setAuthorId(1L);
        dto.setCommunityId(1L);

        PostResponseDto expectedDto = new PostResponseDto();
        expectedDto.setId(1L);
        expectedDto.setCreatedAt(Instant.now());
        expectedDto.setUpdatedAt(Instant.now());
        expectedDto.setTitle("testPost");
        expectedDto.setContent("testContent");
        expectedDto.setAuthorId(1L);
        expectedDto.setCommunityId(1L);
        expectedDto.setStatus(PostStatus.PUBLISHED);
        expectedDto.setEdited(false);

        when(postMapper.toEntity(dto)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponseDto(post)).thenReturn(expectedDto);

        PostResponseDto actualDto = postService.createPost(dto, null);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void testCreatePost_Success_FileExists() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("testPost");
        dto.setContent("testContent");
        dto.setAuthorId(1L);
        dto.setCommunityId(1L);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );

        PostResponseDto expectedDto = new PostResponseDto();
        expectedDto.setId(1L);
        expectedDto.setCreatedAt(Instant.now());
        expectedDto.setUpdatedAt(Instant.now());
        expectedDto.setTitle("testPost");
        expectedDto.setContent("testContent");
        expectedDto.setAuthorId(1L);
        expectedDto.setCommunityId(1L);
        expectedDto.setStatus(PostStatus.PUBLISHED);
        expectedDto.setEdited(false);

        when(postMapper.toEntity(dto)).thenReturn(post);
        when(storageService.store(file)).thenReturn("some/path/hello.txt");
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponseDto(post)).thenReturn(expectedDto);

        PostResponseDto actualDto = postService.createPost(dto, file);

        assertEquals(expectedDto, actualDto);
        verify(storageService, times(1)).store(file);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testCreatePost_Fail_InvalidInput() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("testPost");
        dto.setContent("testContent");
        dto.setAuthorId(1L);

        when(postMapper.toEntity(dto)).thenThrow(new InvalidInputException("Community ID cannot be null"));

        assertThrows(InvalidInputException.class, () -> postService.createPost(dto, null));
    }

    @Test
    void testUpdatePost_Success_NoFile() {
        PostUpdateDto dto = new PostUpdateDto();
        dto.setTitle("updatedTitle");
        dto.setContent("updatedContent");

        PostResponseDto expectedDto = new PostResponseDto();
        expectedDto.setId(1L);
        expectedDto.setTitle("updatedTitle");
        expectedDto.setContent("updatedContent");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponseDto(any(Post.class))).thenReturn(expectedDto);

        PostResponseDto actualDto = postService.updatePost(1L, dto, null);

        assertEquals(expectedDto, actualDto);
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUpdatePost_Success_FileExists() {
        PostUpdateDto dto = new PostUpdateDto();
        dto.setTitle("updatedTitle");
        dto.setContent("updatedContent");

        PostResponseDto expectedDto = new PostResponseDto();
        expectedDto.setId(1L);
        expectedDto.setTitle("updatedTitle");
        expectedDto.setContent("updatedContent");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );

        String oldAttachmentPath = post.getAttachmentPath();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(storageService.store(file)).thenReturn("some/path/hello.txt");
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponseDto(any(Post.class))).thenReturn(expectedDto);

        PostResponseDto actualDto = postService.updatePost(1L, dto, file);

        assertEquals(expectedDto, actualDto);
        verify(postRepository, times(1)).findById(1L);
        verify(storageService, times(1)).delete(oldAttachmentPath);
        verify(storageService, times(1)).store(file);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUpdatePost_Fail_PostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.updatePost(1L, null, null));
    }

    @Test
    void testDeletePost_Success() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L);

        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void testDeletePost_Fail_PostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.deletePost(1L));
    }

}

