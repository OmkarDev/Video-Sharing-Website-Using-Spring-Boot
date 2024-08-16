package com.omkardixit.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omkardixit.main.entities.Subscribe;
import com.omkardixit.main.entities.User;
import com.omkardixit.main.repositories.SubscribeRepository;

@Service
public class SubscribeService {

	@Autowired
	private SubscribeRepository subscribeRepository;

	public String subscribe(User user, User channel) {
		if (user.getId() != channel.getId() && hasSubscribed(user, channel) == false) {
			Subscribe subscribe = new Subscribe();
			subscribe.setSubscribedTo(channel);
			subscribe.setSubscriber(user);
			subscribeRepository.save(subscribe);
		}
		return getFormattedSubscriberCount(channel);
	}

	public String unsubscribe(User user, User channel) {
		if (user.getId() != channel.getId() && hasSubscribed(user, channel)) {
			Subscribe subscribe = subscribeRepository.findBySubscriberAndSubscribedTo(user, channel).get();
			subscribeRepository.delete(subscribe);
		}
		return getFormattedSubscriberCount(channel);
	}

	public int getNumberOfSubscribers(User channel) {
		return subscribeRepository.findBySubscribedTo(channel).size();
	}

	public boolean hasSubscribed(User user, User channel) {
		return subscribeRepository.findBySubscriberAndSubscribedTo(user, channel).isPresent();
	}

	public String getFormattedSubscriberCount(User channel) {
		int numberOfSubscribers = getNumberOfSubscribers(channel);
		if (numberOfSubscribers == 1) {
			return "1 subscriber";
		}
		return numberOfSubscribers + " subscribers";
	}
}
