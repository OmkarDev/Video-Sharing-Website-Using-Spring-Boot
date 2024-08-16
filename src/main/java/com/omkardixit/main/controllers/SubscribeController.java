package com.omkardixit.main.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.exceptions.ResourceNotFoundException;
import com.omkardixit.main.services.LikeService;
import com.omkardixit.main.services.SubscribeService;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/subscribers")
public class SubscribeController {

	@Autowired
	VideoService videoService;

	@Autowired
	UserService userService;

	@Autowired
	SubscribeService subscribeService;

	@PostMapping("/{username}")
	@ResponseBody
	public String subscribe(@PathVariable String username, @RequestParam("action") String action,
			Principal principal) {
		User channel = userService.getUserbyUsername(username).orElseThrow(() -> new ResourceNotFoundException());
		String subscribers = subscribeService.getFormattedSubscriberCount(channel);
		if (principal == null) {
			return subscribers;
		}
		User user = userService.getUserbyUsername(principal.getName())
				.orElseThrow(() -> new ResourceNotFoundException());

		if (action.equals("subscribe")) {
			subscribers = subscribeService.subscribe(user, channel);
		} else if (action.equals("unsubscribe")) {
			subscribers = subscribeService.unsubscribe(user, channel);
		}
		return subscribers;
	}
}
