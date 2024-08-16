package com.omkardixit.main.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<User> getUserbyUsername(String username){
		var list = userRepository.findByUsername(username);
		if (list.size() == 1) {
			return Optional.of(list.get(0));
		} else {
			return Optional.ofNullable(null);
		}
	}

	public void addUser(String username, String password, String channelName) {
		User user = new User();
		user.setUsername(username);
		//TODO encrypt it
		user.setPassword(password);
		user.setChannelName(channelName);
		userRepository.save(user);
	}

}
