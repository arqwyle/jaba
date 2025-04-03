package com.example.lab6;

import com.example.lab6.model.Comment;
import com.example.lab6.model.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
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
    private TestRestTemplate restTemplate;

    @Test
    void testAddAndGetVideo() {
        Video video = new Video();
        video.setName("Test Video");

        ResponseEntity<Video> postResponse = restTemplate.postForEntity("/api/v3/video", video, Video.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        Long videoId = postResponse.getBody().getId();

        ResponseEntity<Video> getResponse = restTemplate.getForEntity("/api/v3/video/" + videoId, Video.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Test Video", getResponse.getBody().getName());
    }

    @Test
    void testAddAndGetComment() {
        Video video = new Video();
        video.setName("Test Video");

        ResponseEntity<Video> videoResponse = restTemplate.postForEntity("/api/v3/video", video, Video.class);
        Long videoId = videoResponse.getBody().getId();

        Comment comment = new Comment();
        comment.setText("Test Comment");
        comment.setUserId(1L);

        ResponseEntity<Comment> commentResponse = restTemplate.postForEntity(
                "/api/v3/video/" + videoId + "/comments", comment, Comment.class);
        assertEquals(HttpStatus.OK, commentResponse.getStatusCode());

        Long commentId = commentResponse.getBody().getId();

        ResponseEntity<Comment> getResponse = restTemplate.getForEntity(
                "/api/v3/video/" + videoId + "/comments/" + commentId, Comment.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Test Comment", getResponse.getBody().getText());
    }
}
