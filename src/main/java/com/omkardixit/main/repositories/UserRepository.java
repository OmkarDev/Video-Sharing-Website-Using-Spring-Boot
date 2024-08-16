package com.omkardixit.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omkardixit.main.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public List<User> findByUsername(String username);
	
}
