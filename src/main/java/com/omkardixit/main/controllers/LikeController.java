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
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/likes")
public class LikeController {

	@Autowired
	VideoService videoService;

	@Autowired
	UserService userService;

	@Autowired
	LikeService likeService;

	@PostMapping("/{permalink}")
	@ResponseBody
	public String likedVideo(@PathVariable String permalink, @RequestParam("action") String action,
			Principal principal) {
		Video video = videoService.getVideoByPermalink(permalink).orElseThrow(() -> new ResourceNotFoundException());
		int likes = video.getLikes().size();
		if (principal == null) {
			return Integer.toString(likes);
		}
		User user = userService.getUserbyUsername(principal.getName())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (action.equals("like")) {
			likes = likeService.likeVideoFromUser(video, user);
		}else if(action.equals("unlike")) {
			likes = likeService.unlikeVideoFromUser(video, user);
		}

		return Integer.toString(likes);
	}

}
