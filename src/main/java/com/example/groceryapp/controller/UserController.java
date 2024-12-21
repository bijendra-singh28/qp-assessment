package com.example.groceryapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.groceryapp.model.BookedItem;
import com.example.groceryapp.model.Groceries;
import com.example.groceryapp.repository.BookedItemsRepository;
import com.example.groceryapp.repository.GroceriesRepository;
import com.example.groceryapp.service.GroceryAppService;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    GroceriesRepository groceriesRepository;

    @Autowired
    BookedItemsRepository bookedItemsRepository;
    
    @GetMapping("/groceries")
    public String viewGroceries(Model model) {
        List<Groceries> groceries = groceriesRepository.findByIsActive(1);
        model.addAttribute("groceries", groceries);
        return "user-groceries";
    }

    @PostMapping("/book")
    public String bookGroceries(@RequestParam("groceryIds") List<Long> groceryIds,
                                @RequestParam("quantities") List<Integer> quantities,
                                Model model,
                                RedirectAttributes redirectAttributes) {
    	
    	groceryIds.removeIf(item -> item == null);
    	quantities.removeIf(item -> item == null);
        
    	if (groceryIds == null || quantities == null || groceryIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "No groceries selected for booking.");
            return "user-groceries";  // Redirect to the groceries page with an error
        }

        // Ensure the lists have the same size
        if (groceryIds.size() != quantities.size()) {
          
            redirectAttributes.addFlashAttribute("message", "Mismatch between selected groceries and quantities.");
            return "user-groceries";
        }

        // Validate quantities and stock availability
        for (int i = 0; i < groceryIds.size(); i++) {
            Long groceryId = groceryIds.get(i);
            Integer requestedQuantity = quantities.get(i);

            if (requestedQuantity == null || requestedQuantity <= 0) {
              
                redirectAttributes.addFlashAttribute("message", "Invalid quantity for grocery ID: " + groceryId);
                return "user-groceries";
            }

            Groceries grocery = groceriesRepository.findGroceriesById(groceryId);
            if (grocery == null) {
        
                redirectAttributes.addFlashAttribute("message", "Grocery not found for ID: " + groceryId);
                return "user-groceries";
            }

            if (requestedQuantity > grocery.getQuantityInStock()) {
                redirectAttributes.addFlashAttribute("message", "Requested quantity for " + grocery.getName() + " exceeds available stock.");
                return "user-groceries";
            }
        }

        // Create and save the booked items if validation passes
        List<BookedItem> bookedItems = new ArrayList<>();
        for (int i = 0; i < groceryIds.size(); i++) {
            Long groceryId = groceryIds.get(i);
            Integer quantity = quantities.get(i);

            
            BookedItem bookedItem = new BookedItem();
            bookedItem.setGrocery(groceriesRepository.findById(groceryId));
            bookedItem.setBookedBy("USER");  // You could make this dynamic if needed
            bookedItem.setQuantity(quantity);
            bookedItem.setBookingDate(new Timestamp(System.currentTimeMillis()));

            bookedItems.add(bookedItem);
        }

        bookedItemsRepository.saveAll(bookedItems);

        // Update stock
        for (int i = 0; i < groceryIds.size(); i++) {
            Long groceryId = groceryIds.get(i);
            Integer quantity = quantities.get(i);

            Groceries grocery = groceriesRepository.findGroceriesById(groceryId);
            if (grocery != null) {
                grocery.setQuantityInStock(grocery.getQuantityInStock() - quantity);
                groceriesRepository.save(grocery);
            }
        }
        redirectAttributes.addFlashAttribute("message", "Selected groceries have been booked.");
        return "redirect:/user-groceries?success=true";
    }

    @GetMapping("/view-booked-groceries")
    public String viewBookedGroceries(Model model,Principal principal) {
        String username = "USER"; // Replace with actual logic to get logged-in username
        List<BookedItem> bookedItems = bookedItemsRepository.findByBookedBy(username);
        String role = principal.getName(); // Example service call
        System.out.println("ROLE :: "+role);
        model.addAttribute("bookedItems", bookedItems);
        model.addAttribute("userRole", role);
        return "view-booked-groceries";
    }
}
