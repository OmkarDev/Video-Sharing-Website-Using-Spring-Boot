package com.omkardixit.main.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.entities.Subscribe;
import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionsController {

	@Autowired
	VideoService videoService;

	@Autowired
	UserService userService;

	@GetMapping("")
	public String subscription(Model model, Principal principal) {
		User user = userService.getUserbyUsername(principal.getName()).get();
		List<Video> videos = new ArrayList<>();
		for (Subscribe subscribe : user.getSubscriptions()) {
			videos.addAll(videoService.getAllPublicVideosByUser(subscribe.getSubscribedTo()));
		}

		Collections.sort(videos, (a, b) -> {
			return b.getId() - a.getId();
		});

		model.addAttribute("videos", videos);
		model.addAttribute("user", user.getUsername());
		model.addAttribute("signedIn", "true");
		model.addAttribute("showChannelName","true");
		return "subscriptions";
	}

}
