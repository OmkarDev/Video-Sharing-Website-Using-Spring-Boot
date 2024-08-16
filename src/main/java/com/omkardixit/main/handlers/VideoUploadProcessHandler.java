package com.omkardixit.main.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.omkardixit.main.entities.Video;
import com.omkardixit.main.services.VideoService;

import jakarta.annotation.PostConstruct;

@Component
public class VideoUploadProcessHandler {

	@Value("${videoFolder.path}")
	private String VIDEO_FOLDER;

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	private VideoService videoService;

	double totalDuration = 300;
	double totalTimeElapsed = 0;
	int processDone = 0;

	public boolean uploadVideo(MultipartFile videoFile, String permalink) {
		String videoFolder = VIDEO_FOLDER + permalink + "/";
		try {
			Files.createDirectory(Path.of(videoFolder));
			Files.createDirectory(Path.of(videoFolder + "144p/"));
			Files.createDirectory(Path.of(videoFolder + "480p/"));
			Files.createDirectory(Path.of(videoFolder + "720p/"));
			Files.write(Path.of(videoFolder + "video.mp4"), videoFile.getBytes());
			saveThumbnail(videoFolder, Files.readAllBytes(Path.of(VIDEO_FOLDER + "thumbnail.png")));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void saveThumbnail(String videoFolder, byte[] bytes) throws IOException {
		Files.write(Path.of(videoFolder + "thumbnail.png"), bytes);
	}

	public void processVideo(Video video) {
		String userId = Integer.toString(video.getUser().getId());
		String permalink = video.getPermalink();
		if (videoService.getProcesingStatus(permalink).equals("canStart")) {
			ProcessBuilder build = new ProcessBuilder();
			String videoFolder = VIDEO_FOLDER + video.getPermalink() + "/";
			videoService.setProocessingStatus(permalink, "inProgress");
			try {
				processDone = 0;
				convertTo144pStream(build, videoFolder, userId, permalink);
				convertTo480pStream(build, videoFolder, userId, permalink);
				convertTo720pStream(build, videoFolder, userId, permalink);
			} catch (IOException e) {
				System.out.println(build.redirectErrorStream());
				e.printStackTrace();
			}
			videoService.setProocessingStatus(permalink, "completed");
		}
		if (videoService.getProcesingStatus(permalink).equals("completed")) {
			template.convertAndSendToUser(userId, "/video/" + permalink + "/progress", 100.00);
		}
	}

	private void convertTo144pStream(ProcessBuilder builder, String videoFolder, String userId, String permalink)
			throws IOException {
		String inputFilePath = videoFolder + "video.mp4";
		String outputDir = videoFolder + "144p";
		builder.command("ffmpeg", "-i", inputFilePath,
                "-vf", "scale=256:144",
                "-g", "60",
                "-hls_time", "2",
				"-hls_list_size", "0", "-hls_segment_size", "500000", outputDir + "/output_144p.m3u8");
		progressOfBuilder(builder, userId, permalink);
	}

	private void convertTo480pStream(ProcessBuilder builder, String videoFolder, String userId, String permalink)
			throws IOException {
		String inputFilePath = videoFolder + "video.mp4";
		String outputDir = videoFolder + "480p";
		builder.command("ffmpeg", "-i", inputFilePath,
                "-vf", "scale=854:480",
                "-g", "60",
                "-hls_time", "2",
				"-hls_list_size", "0", "-hls_segment_size", "500000", outputDir + "/output_480p.m3u8");
		progressOfBuilder(builder, userId, permalink);
	}

	private void convertTo720pStream(ProcessBuilder builder, String videoFolder, String userId, String permalink)
			throws IOException {
		String inputFilePath = videoFolder + "video.mp4";
		String outputDir = videoFolder + "720p";
		builder.command("ffmpeg", "-i", inputFilePath,
                "-vf", "scale=1280:720",
                "-g", "60",
                "-hls_time", "2",
				"-hls_list_size", "0", "-hls_segment_size", "500000", outputDir + "/output_720p.m3u8");
		progressOfBuilder(builder, userId, permalink);
	}

	private void progressOfBuilder(ProcessBuilder builder, String userId, String permalink) throws IOException {
		builder.redirectErrorStream(true);
		Process process = builder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		double duration = Double.MAX_VALUE;
		while ((line = reader.readLine()) != null) {
			if (line.contains("Duration:")) {
				duration = convertToDuration(extractDuration(line));
				if (duration == 0) {
					duration = 1;
				}
			}
			if (line.contains("time=")) {
				long timeElapsed = convertToDuration(extractTimeElapsed(line));
				double percentage = (timeElapsed / duration) * 100;
				percentage = Math.min(100.0, percentage);
				totalTimeElapsed = (processDone * 100) + percentage;
				double totalPercentage = (totalTimeElapsed / totalDuration) * 100;
				template.convertAndSendToUser(userId, "/video/" + permalink + "/progress", totalPercentage);
			}
		}
		processDone++;
	}

	private String extractTimeElapsed(String line) {
		int index = line.indexOf("time=") + "time=".length();
		return line.substring(index, line.indexOf(" ", index));
	}

	private String extractDuration(String line) {
		int durationIndex = line.indexOf("Duration: ") + "Duration: ".length();
		String durationPart = line.substring(durationIndex, line.indexOf(',', durationIndex)).trim();
		return durationPart;
	}

	private long convertToDuration(String timeString) {
		String[] timeParts = timeString.split(":");
		int hours = Integer.parseInt(timeParts[0]);
		int minutes = Integer.parseInt(timeParts[1]);

		String[] secondParts = timeParts[2].split("\\.");
		int seconds = Integer.parseInt(secondParts[0]);
		int milliseconds = Integer.parseInt(secondParts[1]) * 10;

		long totalMilliseconds = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;

		return totalMilliseconds;
	}

	public String generateRandomCharacters(int len) {
		StringBuilder s = new StringBuilder();
		char[] alphabets = new char[52];
		for (int i = 0; i < 26; i++) {
			alphabets[i] = (char) ('a' + i);
		}
		for (int i = 0; i < 26; i++) {
			alphabets[i + 26] = (char) ('A' + i);
		}
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			int index = random.nextInt(52);
			s.append(alphabets[index]);
		}
		return s.toString();
	}
}
