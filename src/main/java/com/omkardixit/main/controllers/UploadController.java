package com.omkardixit.main.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.exceptions.BadRequestException;
import com.omkardixit.main.exceptions.ResourceNotFoundException;
import com.omkardixit.main.handlers.VideoUploadProcessHandler;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	VideoUploadProcessHandler vuph;

	@Autowired
	UserService userService;

	@Autowired
	private VideoService videoService;

	@GetMapping("")
	public String uploadVideo() {
		return "upload.html";
	}

	@PostMapping("")
	public String uploadVideoFile(@RequestParam MultipartFile videoFile, Principal principal) {
		User user = userService.getUserbyUsername(principal.getName()).orElseThrow(() -> new BadRequestException());
		if (videoFile == null || videoFile.isEmpty() || !videoFile.getContentType().contains("video/mp4")) {
			throw new BadRequestException();
		}
		String permalink = vuph.generateRandomCharacters(8);
		boolean uploaded = vuph.uploadVideo(videoFile, permalink);
		if (!uploaded) {
			throw new BadRequestException();
		}
		Video video = new Video();
		video.setTitle("");
		video.setDescription("");
		video.setViews((int) (Math.random() * 1000));
		video.setPermalink(permalink);
		video.setVisibility(0);
		video.setProcesingStatus("canStart");
		video.setUser(user);
		videoService.createVideo(video);
		System.out.println("Video Uploaded: " + permalink);
		return "redirect:/edit/" + permalink;
	}

}
