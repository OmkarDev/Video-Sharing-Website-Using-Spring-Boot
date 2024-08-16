package com.omkardixit.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omkardixit.main.entities.Subscribe;
import com.omkardixit.main.entities.User;
import java.util.List;



public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{

	Optional<Subscribe> findBySubscriberAndSubscribedTo(User subscriber, User subscribedTo);

	List<Subscribe> findBySubscriber(User user);
	List<Subscribe> findBySubscribedTo(User channel);
}
