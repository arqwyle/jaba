package com.example.lab6.service;

import com.example.lab6.model.Comment;
import com.example.lab6.model.Video;
import com.example.lab6.repository.CommentRepository;
import com.example.lab6.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MewtubeServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private MewtubeService mewtubeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddVideo() {
        Video video = new Video();
        video.setName("Test Video");

        when(videoRepository.save(any(Video.class))).thenReturn(video);

        Video result = mewtubeService.addVideo(video);

        assertNotNull(result);
        assertEquals("Test Video", result.getName());
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void testUpdateVideo() {
        Video video = new Video();
        video.setId(1L);
        video.setName("Updated Video");

        when(videoRepository.existsById(1L)).thenReturn(true);
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        Video result = mewtubeService.updateVideo(video);

        assertNotNull(result);
        assertEquals("Updated Video", result.getName());
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void testGetVideoById() {
        Video video = new Video();
        video.setId(1L);
        video.setName("Test Video");

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        Video result = mewtubeService.getVideoById(1L);

        assertNotNull(result);
        assertEquals("Test Video", result.getName());
        verify(videoRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteVideo() {
        when(videoRepository.existsById(1L)).thenReturn(true);

        mewtubeService.deleteVideo(1L);

        verify(videoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddComment() {
        Video video = new Video();
        video.setId(1L);

        Comment comment = new Comment();
        comment.setText("Test Comment");

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = mewtubeService.addComment(1L, comment);

        assertNotNull(result);
        assertEquals("Test Comment", result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Updated Comment");

        when(commentRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = mewtubeService.updateComment(1L, comment);

        assertNotNull(result);
        assertEquals("Updated Comment", result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testGetCommentById() {
        Video video = new Video();
        video.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setVideo(video);
        comment.setText("Test Comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = mewtubeService.getCommentById(1L, 1L);

        assertNotNull(result);
        assertEquals("Test Comment", result.getText());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteComment() {
        Video video = new Video();
        video.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUserId(1L);
        comment.setVideo(video);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        mewtubeService.deleteComment(1L, 1L, 1L);

        verify(commentRepository, times(1)).delete(comment);
    }
}