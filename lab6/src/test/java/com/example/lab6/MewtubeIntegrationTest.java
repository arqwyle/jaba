package com.example.lab6;

import com.example.lab6.model.Comment;
import com.example.lab6.model.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class MewtubeIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("mewtube")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddAndGetVideo() throws Exception {
        Video video = new Video();
        video.setName("Test Video");

        MvcResult postResult = mockMvc.perform(post("/api/v3/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andReturn();

        Video createdVideo = objectMapper.readValue(postResult.getResponse().getContentAsString(), Video.class);
        Long videoId = createdVideo.getId();

        mockMvc.perform(get("/api/v3/video/{id}", videoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Video"));
    }

    @Test
    void testAddAndGetComment() throws Exception {
        Video video = new Video();
        video.setName("Test Video");

        MvcResult videoPostResult = mockMvc.perform(post("/api/v3/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andReturn();

        Video createdVideo = objectMapper.readValue(videoPostResult.getResponse().getContentAsString(), Video.class);
        Long videoId = createdVideo.getId();

        Comment comment = new Comment();
        comment.setText("Test Comment");
        comment.setUserId(1L);

        MvcResult commentPostResult = mockMvc.perform(post("/api/v3/video/{videoId}/comments", videoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andReturn();

        Comment createdComment = objectMapper.readValue(commentPostResult.getResponse().getContentAsString(), Comment.class);
        Long commentId = createdComment.getId();

        mockMvc.perform(get("/api/v3/video/{videoId}/comments/{commentId}", videoId, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test Comment"));
    }
}
