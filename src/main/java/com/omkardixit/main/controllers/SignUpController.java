package com.omkardixit.main.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.omkardixit.main.entities.User;
import com.omkardixit.main.services.UserService;

@Controller
@RequestMapping("/signup")
public class SignUpController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public String signup(Principal principal, Model model) {
		if (principal != null) {
			return "redirect:/logout";
		}
		User user = new User();
		model.addAttribute("user", user);
		return "signup";
	}

	@PostMapping("")
	public String register(@ModelAttribute User user) {
		userService.addUser(user.getUsername(), user.getPassword(), user.getChannelName());
		return "redirect:/login";
	}
}
