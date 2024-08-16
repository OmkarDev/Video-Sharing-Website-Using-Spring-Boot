package com.omkardixit.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omkardixit.main.entities.Like;
import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.repositories.LikeRepository;
import com.omkardixit.main.repositories.VideoRepository;

@Service
public class LikeService {

	@Autowired
	LikeRepository likeRepository;

//	@Transactional
	public int likeVideoFromUser(Video video, User user) {
		if (hasUserLiked(user, video) == false) {
			Like like = new Like();
			like.setVideo(video);
			like.setUser(user);
			likeRepository.save(like);
		}
		return numberOfLikes(video);
	}

//	@Transactional
	public int unlikeVideoFromUser(Video video, User user) {
		if (hasUserLiked(user, video)) {
			Like like = likeRepository.findByUserAndVideo(user, video).get();
			likeRepository.delete(like);
		}
		return numberOfLikes(video);
	}

	public boolean hasUserLiked(User user, Video video) {
		return likeRepository.findByUserAndVideo(user, video).isPresent();
	}

	public int numberOfLikes(Video video) {
		return likeRepository.findByVideo(video).size();
	}
	
}
