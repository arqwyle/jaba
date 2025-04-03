package com.example.lab6.controller;

import com.example.lab6.model.Comment;
import com.example.lab6.model.Video;
import com.example.lab6.service.MewtubeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/video")
public class MewtubeController {

    private static final Logger logger = LoggerFactory.getLogger(MewtubeController.class);

    private final MewtubeService mewtubeService;

    public MewtubeController(MewtubeService mewtubeService) {
        this.mewtubeService = mewtubeService;
    }

    @PostMapping
    public ResponseEntity<Video> addVideo(@RequestBody Video video) {
        logger.info("Adding new video: {}", video);
        Video addedVideo = mewtubeService.addVideo(video);
        return new ResponseEntity<>(addedVideo, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Video> updateVideo(@RequestBody Video video) {
        logger.info("Updating video: {}", video);
        Video updatedVideo = mewtubeService.updateVideo(video);
        return new ResponseEntity<>(updatedVideo, HttpStatus.OK);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long videoId) {
        logger.info("Fetching video with ID: {}", videoId);
        Video video = mewtubeService.getVideoById(videoId);
        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        logger.info("Deleting video with ID: {}", videoId);
        mewtubeService.deleteVideo(videoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{videoId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long videoId, @RequestBody Comment comment) {
        logger.info("Adding comment to video ID {}: {}", videoId, comment);
        Comment addedComment = mewtubeService.addComment(videoId, comment);
        return new ResponseEntity<>(addedComment, HttpStatus.OK);
    }

    @PutMapping("/{videoId}/comments")
    public ResponseEntity<Comment> updateComment(@PathVariable Long videoId, @RequestBody Comment comment) {
        logger.info("Updating comment for video ID {}: {}", videoId, comment);
        Comment updatedComment = mewtubeService.updateComment(videoId, comment);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @GetMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long videoId, @PathVariable Long commentId) {
        logger.info("Fetching comment with ID {} for video ID {}", commentId, videoId);
        Comment comment = mewtubeService.getCommentById(videoId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{videoId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long videoId,
            @PathVariable Long commentId,
            @RequestHeader Long userId) {
        logger.info("Deleting comment with ID {} for video ID {} by user ID {}", commentId, videoId, userId);
        mewtubeService.deleteComment(videoId, commentId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
