package com.omkardixit.main.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.omkardixit.main.entities.Video;
import com.omkardixit.main.services.VideoService;

@Controller
public class HomeController {

	@Autowired
	VideoService videoService;
	
	@GetMapping("/")
	public String home(Principal principal, Model model) {
		List<Video> videos = videoService.getAllPublicVideos();
		model.addAttribute("videos", videos);	
		model.addAttribute("showChannelName","true");	
		if (principal == null) {
			model.addAttribute("signedIn", "false");
			return "home";
		}
		model.addAttribute("signedIn", "true");
		model.addAttribute("user", principal.getName());
		return "home";
	}

}
