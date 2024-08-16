package com.omkardixit.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.omkardixit.main.entities.Video;
import com.omkardixit.main.exceptions.ResourceNotFoundException;
import com.omkardixit.main.handlers.VideoUploadProcessHandler;
import com.omkardixit.main.services.VideoService;

@Controller
public class VideoProcessWebSocketController {

	@Autowired
	VideoUploadProcessHandler vuph;

	@Autowired
	private VideoService videoService;

	@MessageMapping("/process-video")
	public void videoProcess(String permalink) {
		Video video = videoService.getVideoByPermalink(permalink).orElseThrow(() -> new ResourceNotFoundException());
		vuph.processVideo(video);
	}
}
