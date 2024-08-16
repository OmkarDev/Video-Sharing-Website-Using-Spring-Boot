const likeIcon = document.getElementById('like-icon');
const likeCountElement = document.getElementById('like-count');
let liked = hasLiked == "true";

document.addEventListener('DOMContentLoaded', function() {
	if (hasLiked == "true") {
		likeIcon.classList.remove('bi-suit-heart');
		likeIcon.classList.add('bi-suit-heart-fill', 'liked');
	}
});

function toggleLike() {
	if(user == "null"){
		window.location.replace("/login");
		return;
	}
	const csrfToken = document.querySelector('input[name="_csrf"]').getAttribute('value');
	liked = !liked;
	if (liked) {
		action = "like";
		likeIcon.classList.remove('bi-suit-heart');
		likeIcon.classList.add('bi-suit-heart-fill', 'liked');
	} else {
		action = "unlike";
		likeIcon.classList.remove('bi-suit-heart-fill', 'liked');
		likeIcon.classList.add('bi-suit-heart');
	}
	fetch("/likes/" + permalink,
		{
			method: "POST",
			headers: {
				'X-CSRF-TOKEN': csrfToken,
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: new URLSearchParams({ action: action }).toString()

		}
	).then(response => response.text())
		.then(data => {
			likeCount = data;
			likeCountElement.textContent = likeCount;
		});
}