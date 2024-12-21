package com.example.groceryapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AdminController {

	 @GetMapping("/admin/dashboard")
	 public String adminDashboard(Model model) {
	        model.addAttribute("options", new String[]{
	            "Add Grocery",
	            "View Existing Grocery",
	            "Remove Grocery",
	            "Update Details",
	            "View Booked Groceries"
	        });
	        return "admin-dashboard";
	 }
	 
	 @GetMapping("/user/dashboard")
	 public String userDashboard(Model model) {
	        model.addAttribute("options", new String[]{
	            "User Groceries",
	            "View Booked Groceries"
	        });
	        return "user-dashboard";
	 }
}
