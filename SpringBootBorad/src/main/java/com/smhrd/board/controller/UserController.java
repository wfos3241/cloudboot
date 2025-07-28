package com.smhrd.board.controller;

import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	
	@Autowired
    private UserService userService;
	
	@PostMapping("/register.do")
	public String register(@RequestParam String id, @RequestParam String password, @RequestParam String name, @RequestParam int age) {
		
		// 3. Service 연결
		
		UserEntity entity = new UserEntity();
		entity.setId(id);
		entity.setPassword(password);
		entity.setName(name);
		entity.setAge(age);
		
		String result = userService.register(entity);
		
		if (result.equals("success")) {
		
			return "redirect:/login";
		
		}
		
		else {
			
			return "redirect:/register";
			
		}
		
	}
	
	// 로그인 기능
		
	@PostMapping("/login.do")
	public String login(@RequestParam String id, @RequestParam String password, HttpSession session) {
			
			UserEntity entity = new UserEntity();
			entity.setId(id);
			entity.setPassword(password);
			
			UserEntity user = userService.login(id, password);
		
			if (user != null) {
				
				session.setAttribute("user", user);
				return "redirect:/";
				
			}
			
			else {
				
				return "redirect:/login";
				
			}
			
	}
	
	// 로그아웃 기능
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.removeAttribute("user");
		
		return "redirect:/";
		
	}
	
}
