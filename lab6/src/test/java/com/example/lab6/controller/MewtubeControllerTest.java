package com.example.lab6.controller;

import com.example.lab6.model.Comment;
import com.example.lab6.model.Video;
import com.example.lab6.service.MewtubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MewtubeControllerTest {

    private MockMvc mockMvc;

    private final MewtubeService mewtubeService = Mockito.mock(MewtubeService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MewtubeController controller = new MewtubeController(mewtubeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAddVideo() throws Exception {
        Video video = new Video();
        video.setName("Test Video");

        when(mewtubeService.addVideo(any(Video.class))).thenReturn(video);

        mockMvc.perform(post("/api/v3/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Video"));

        verify(mewtubeService, times(1)).addVideo(any(Video.class));
    }

    @Test
    void testUpdateVideo() throws Exception {
        Video video = new Video();
        video.setId(1L);
        video.setName("Updated Video");

        when(mewtubeService.updateVideo(any(Video.class))).thenReturn(video);

        mockMvc.perform(put("/api/v3/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Video"));

        verify(mewtubeService, times(1)).updateVideo(any(Video.class));
    }

    @Test
    void testGetVideoById() throws Exception {
        Video video = new Video();
        video.setId(1L);
        video.setName("Test Video");

        when(mewtubeService.getVideoById(1L)).thenReturn(video);

        mockMvc.perform(get("/api/v3/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Video"));

        verify(mewtubeService, times(1)).getVideoById(1L);
    }

    @Test
    void testDeleteVideo() throws Exception {
        doNothing().when(mewtubeService).deleteVideo(1L);

        mockMvc.perform(delete("/api/v3/video/{videoId}", 1L))
                .andExpect(status().isOk());

        verify(mewtubeService, times(1)).deleteVideo(1L);
    }

    @Test
    void testAddComment() throws Exception {
        Comment comment = new Comment();
        comment.setText("Test Comment");

        when(mewtubeService.addComment(eq(1L), any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(1));

        mockMvc.perform(post("/api/v3/video/{videoId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test Comment"));

        verify(mewtubeService, times(1)).addComment(eq(1L), any(Comment.class));
    }

    @Test
    void testUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Updated Comment");

        when(mewtubeService.updateComment(eq(1L), any(Comment.class))).thenReturn(comment);

        mockMvc.perform(put("/api/v3/video/{videoId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated Comment"));

        verify(mewtubeService, times(1)).updateComment(eq(1L), any(Comment.class));
    }

    @Test
    void testGetCommentById() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");

        when(mewtubeService.getCommentById(1L, 1L)).thenReturn(comment);

        mockMvc.perform(get("/api/v3/video/{videoId}/comments/{commentId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test Comment"));

        verify(mewtubeService, times(1)).getCommentById(1L, 1L);
    }

    @Test
    void testDeleteComment() throws Exception {
        doNothing().when(mewtubeService).deleteComment(1L, 1L, 1L);

        mockMvc.perform(delete("/api/v3/video/{videoId}/comments/{commentId}", 1L, 1L)
                        .header("userId", 1L))
                .andExpect(status().isOk());

        verify(mewtubeService, times(1)).deleteComment(1L, 1L, 1L);
    }
}
