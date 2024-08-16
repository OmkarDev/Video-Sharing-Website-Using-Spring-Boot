package com.omkardixit.main.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.exceptions.BadRequestException;
import com.omkardixit.main.exceptions.ForbiddenException;
import com.omkardixit.main.exceptions.ResourceNotFoundException;
import com.omkardixit.main.services.LikeService;
import com.omkardixit.main.services.SubscribeService;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/video")
public class VideoController {
	@Autowired
	private UserService userService;

	@Autowired
	private VideoService videoService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private SubscribeService subscribeService;

	@GetMapping("/{permalink}")
	public String watchVideo(@PathVariable String permalink, Model model, Principal principal) {
		Video video = videoService.getVideoByPermalink(permalink).orElseThrow(() -> new ResourceNotFoundException());
		// TODO SHOULD BE ABLE TO WATCH IF NOT LOGGED IN
		User user = null;
		if (principal != null) {
			user = userService.getUserbyUsername(principal.getName()).get();
		}
//		Optional<User> optionalUser = userService.getUserbyUsername(principal.getName());
		User channel = video.getUser();
		String username = "null";
		if (user != null) {
			model.addAttribute("signedIn", "true");
			username = user.getUsername();
			model.addAttribute("hasLiked", likeService.hasUserLiked(user, video));
			model.addAttribute("hasSubscribed", subscribeService.hasSubscribed(user, channel));
		} else {
			model.addAttribute("signedIn", "false");
		}
		if (user == null || video.getUser().getId() != user.getId()) {
			if (video.getVisibility() == 0) {
				throw new ForbiddenException();
			}
		}

		if (!video.getProcesingStatus().equals("completed")) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("channel_name", video.getUser().getChannelName());
		model.addAttribute("creator", video.getUser().getUsername());
		model.addAttribute("user", username);
		model.addAttribute("views", video.getViews());
		model.addAttribute("title", video.getTitle());
		model.addAttribute("description", video.getDescription());
		model.addAttribute("permalink", permalink);
		model.addAttribute("likeCount", likeService.numberOfLikes(video));
		model.addAttribute("subscriberCount", subscribeService.getFormattedSubscriberCount(channel));
		return "video.html";
	}

}
