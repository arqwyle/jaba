package com.example.lab6.repository;

import com.example.lab6.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
