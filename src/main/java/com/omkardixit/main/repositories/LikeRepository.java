package com.omkardixit.main.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omkardixit.main.entities.Like;
import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;


public interface LikeRepository extends JpaRepository<Like, Integer>{

	Optional<Like> findByUserAndVideo(User user,Video video);
	
	List<Like> findByVideo(Video video);
	
}
