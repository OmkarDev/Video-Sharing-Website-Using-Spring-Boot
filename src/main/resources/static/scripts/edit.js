'use strict'


const client = new StompJs.Client({
	brokerURL: "ws://localhost:8081/ws-youtube-clone"
});

var videoUploadPage;

function updateThumbnail(event) {
	const file = event.target.files[0];
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			const thumbnailPreview = document.getElementById('thumbnailPreview');
			thumbnailPreview.src = e.target.result;
		};
		reader.readAsDataURL(file);
	}
}

function updateProgressBar(value) {
	const progressBar = document.getElementById('progressBar');
	progressBar.style.width = value + '%';
	progressBar.setAttribute('aria-valuenow', value);
	progressBar.textContent = value + '%';

	if (value == 100.00) {
		document.getElementById('progressContainer').style.display = 'none';
		document.getElementById('videoLinkContainer').style.display = 'block';
		document.getElementById('videoLink').href = '/video/' + permalink;
		document.getElementById('videoLink').innerText = '/video/' + permalink;
	}
}

document.addEventListener('DOMContentLoaded', function() {
	client.activate();
	videoUploadPage = document.getElementById("videoUploadPage");
});


client.onConnect = (frame) => {
	console.log("CONNECTED");
	client.publish({
		destination: "/app/process-video",
		body: permalink
	});
	client.subscribe("/user/" + userId + "/video/" + permalink + "/progress", (payload) => {
		let progress = JSON.parse(payload.body);
		progress = (Math.round(progress * 100) / 100).toFixed(2);
		updateProgressBar(progress);
	});
};