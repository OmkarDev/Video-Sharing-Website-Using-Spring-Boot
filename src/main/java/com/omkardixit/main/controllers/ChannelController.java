package com.omkardixit.main.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.exceptions.ResourceNotFoundException;
import com.omkardixit.main.services.SubscribeService;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	@Autowired
	private VideoService videoService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SubscribeService subscribeService;

	@GetMapping("/{creatorString}")
	public String channel(@PathVariable String creatorString, Model model, Principal principal) {
		User creator = userService.getUserbyUsername(creatorString).orElseThrow(() -> new ResourceNotFoundException());
		List<Video> videos = videoService.getAllPublicVideosByUser(creator);

		Collections.sort(videos, (a, b) -> {
			return b.getId() - a.getId();
		});

		String username = "null";
		if (principal != null) {
			username = principal.getName();
			model.addAttribute("signedIn", "true");
			User user = userService.getUserbyUsername(username).get();
			model.addAttribute("hasSubscribed", subscribeService.hasSubscribed(user, creator));
		} else {
			model.addAttribute("signedIn", "false");
		}

		model.addAttribute("videos", videos);
		model.addAttribute("creator", creator.getUsername());
		model.addAttribute("user", username);
		model.addAttribute("subscriberCount", subscribeService.getFormattedSubscriberCount(creator));
		model.addAttribute("channelName", creator.getChannelName());
		model.addAttribute("showChannelName", "false");
		return "channel";
	}

}
