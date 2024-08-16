const subscribeButton = document.getElementById('subscribe-button');
const subscriberCountElement = document.getElementById('subscriber-count');
let subscribed = hasSubscribed == "true";

document.addEventListener('DOMContentLoaded', function() {
	if (subscribed) {
		subscribeButton.textContent = 'Subscribed';
		subscribeButton.classList.add('subscribed');
	}
});

function toggleSubscribe() {
	if (user == "null") {
		window.location.replace("/login");
		return;
	}
	const csrfToken = document.querySelector('input[name="_csrf"]').getAttribute('value');
	subscribed = !subscribed;
	if (subscribed) {
		action = "subscribe";
		subscribeButton.textContent = 'Subscribed';
		subscribeButton.classList.add('subscribed');
	} else {
		action = "unsubscribe";
		subscribeButton.textContent = 'Subscribe';
		subscribeButton.classList.remove('subscribed');
	}
	fetch("/subscribers/" + creator,
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
			subscriberCount = data;
			subscriberCountElement.textContent = subscriberCount;
		});

}
