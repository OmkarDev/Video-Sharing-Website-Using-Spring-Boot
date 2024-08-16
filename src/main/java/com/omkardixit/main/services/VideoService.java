package com.omkardixit.main.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.repositories.VideoRepository;

@Service
public class VideoService {

	@Autowired
	private VideoRepository videoRepository;

	public void createVideo(Video video) {
		videoRepository.save(video);
	}

	public void saveChanges(Video video) {
		videoRepository.save(video);
	}

	public boolean hasVideo(String permalink) {
		return videoRepository.existsByPermalink(permalink);
	}

	public Optional<Video> getVideoByPermalink(String permalink) {
		var list = videoRepository.findByPermalink(permalink);
		if (list.size() == 1) {
			return Optional.of(list.get(0));
		}
		return Optional.ofNullable(null);
	}

	public void setProocessingStatus(String permalink, String status) {
		Video video = getVideoByPermalink(permalink).get();
		video.setProcesingStatus(status);
		videoRepository.save(video);
	}

	public String getProcesingStatus(String permalink) {
		Video video = getVideoByPermalink(permalink).get();
		return video.getProcesingStatus();
	}

	public List<Video> getAllVideosByUser(User user) {
		return user.getVideos();
	}

	public List<Video> getAllPublicVideosByUser(User user) {
		List<Video> result = new ArrayList<>();

		for (Video video : getAllVideosByUser(user)) {
			if (video.getVisibility() == 2 && video.getProcesingStatus().equals("completed")) {
				result.add(video);
			}
		}

		return result;
	}

	public void deleteVideo(String permalink) {
		int id = getVideoByPermalink(permalink).get().getId();
		videoRepository.deleteById(id);
	}

	public List<Video> getAllPublicVideos() {
		List<Video> result = new ArrayList<>();
		List<Video> videos = videoRepository.findAll();
		for (Video video : videos) {
			if (video.getVisibility() == 2 && video.getProcesingStatus().equals("completed")) {
				result.add(video);
			}
		}
		return result;
	}

}
