package com.omkardixit.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omkardixit.main.entities.Video;

public interface VideoRepository extends JpaRepository<Video, Integer>{

	List<Video> findByPermalink(String permalink);
	
	boolean existsByPermalink(String permalink);
	
}
