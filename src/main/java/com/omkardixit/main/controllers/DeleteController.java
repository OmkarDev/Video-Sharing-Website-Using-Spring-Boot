package com.omkardixit.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.services.VideoService;

@Controller
@RequestMapping("/delete")
public class DeleteController {

	@Autowired
	VideoService videoService;
	
	@PostMapping("/{permalink}")
	public String delete(@PathVariable String permalink) {
		videoService.deleteVideo(permalink);
		return "redirect:/dashboard";
	}
	
	
}
