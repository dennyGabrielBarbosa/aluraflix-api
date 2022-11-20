package com.challenge.videos.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.videos.modelo.Videos;

public interface VideosRepository extends JpaRepository<Videos, Long> {
	
}