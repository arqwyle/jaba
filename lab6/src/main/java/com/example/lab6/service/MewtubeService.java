package com.example.lab6.service;

import com.example.lab6.model.Comment;
import com.example.lab6.model.Video;
import com.example.lab6.repository.CommentRepository;
import com.example.lab6.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MewtubeService {

    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;

    public MewtubeService(VideoRepository videoRepository, CommentRepository commentRepository) {
        this.videoRepository = videoRepository;
        this.commentRepository = commentRepository;
    }

    public Video addVideo(Video video) {
        return videoRepository.save(video);
    }

    public Video updateVideo(Video video) {
        if (!videoRepository.existsById(video.getId())) {
            throw new IllegalArgumentException("Video not found");
        }
        return videoRepository.save(video);
    }

    public Video getVideoById(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
    }

    public void deleteVideo(Long videoId) {
        videoRepository.deleteById(videoId);
    }

    public Comment addComment(Long videoId, Comment comment) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        comment.setVideo(video);
        comment.setDate(new Date());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long videoId, Comment comment) {
        if (!commentRepository.existsById(comment.getId())) {
            throw new IllegalArgumentException("Comment not found");
        }
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long videoId, Long commentId) {
        return commentRepository.findById(commentId)
                .filter(c -> c.getVideo().getId().equals(videoId))
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    public void deleteComment(Long videoId, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!comment.getVideo().getId().equals(videoId) || !comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }
}
