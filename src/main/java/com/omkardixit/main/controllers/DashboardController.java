package com.omkardixit.main.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.entities.Video;
import com.omkardixit.main.services.UserService;
import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private VideoService videoService;

	@GetMapping("")
	public String dashboard(Model model, Principal principal) {
		User user = userService.getUserbyUsername(principal.getName()).get();
		List<Video> videos = videoService.getAllVideosByUser(user);
		model.addAttribute("videos", videos);
		return "dashboard";
	}
}
