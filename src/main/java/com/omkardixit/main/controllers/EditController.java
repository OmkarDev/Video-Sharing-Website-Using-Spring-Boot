package com.omkardixit.main.controllers;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.omkardixit.main.exceptions.ForbiddenException;
import com.omkardixit.main.exceptions.ResourceNotFoundException;
import com.omkardixit.main.handlers.VideoUploadProcessHandler;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/edit")
public class EditController {

	@Autowired
	private UserService userService;

	@Autowired
	private VideoService videoService;

	@Autowired
	private VideoUploadProcessHandler vuph;
	
	@Value("${videoFolder.path}")
	private String VIDEO_FOLDER;

	@GetMapping("/{permalink}")
	public String processVideo(@PathVariable String permalink, Model model, Principal principal) {
		Video video = videoService.getVideoByPermalink(permalink).orElseThrow(() -> new ResourceNotFoundException());
		User user = userService.getUserbyUsername(principal.getName())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (video.getUser().getId() != user.getId()) {
			throw new ForbiddenException();
		}
		model.addAttribute("userId", user.getId());
		model.addAttribute("permalink", permalink);
		model.addAttribute("video", video);
		return "edit.html";
	}

	@PostMapping("/{permalink}")
	public String saveVideoChanges(@PathVariable String permalink, @RequestParam String title,
			@RequestParam String description, @RequestParam String visibility,
			@RequestParam("thumbnail") MultipartFile thumbnailFile, Principal principal) {
		Video video = videoService.getVideoByPermalink(permalink).orElseThrow(() -> new ResourceNotFoundException());
		User user = userService.getUserbyUsername(principal.getName())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (video.getUser().getId() != user.getId()) {
			throw new ForbiddenException();
		}
		video.setTitle(title);
		video.setDescription(description);
		video.setVisibility(Integer.parseInt(visibility));
		videoService.saveChanges(video);
		if (thumbnailFile == null || thumbnailFile.isEmpty()) {
			return "redirect:/edit/" + permalink;
		}
		try {
			String videoFolder = VIDEO_FOLDER + permalink + "/";
			vuph.saveThumbnail(videoFolder, thumbnailFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/edit/" + permalink;
	}

}
