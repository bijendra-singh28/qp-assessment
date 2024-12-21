package com.example.groceryapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.groceryapp.model.Groceries;
import com.example.groceryapp.repository.GroceriesRepository;
import com.example.groceryapp.service.GroceryAppService;

@Controller
public class GroceryAppController {

	@Autowired
	GroceriesRepository groceryRepository;
	
	@Autowired
	GroceryAppService groceryService;
	
	@GetMapping("/add-grocery")
    public String showAddGroceryForm(Model model) {
        model.addAttribute("groceries", new Groceries());
        return "add-grocery"; 
    }

    @PostMapping("/add-grocery")
    public String addGrocery(@ModelAttribute("groceries") Groceries groceries,RedirectAttributes redirectAttributes) {
        // Save the grocery object to the database
    	groceries.setIsActive(1);
        groceryRepository.save(groceries);
        redirectAttributes.addFlashAttribute("message", "Details are added.");

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/view-existing-grocery")
    public String viewGrocery(Model model) {
    	// Fetch all groceries from the database
        List<Groceries> groceriesList = groceryRepository.findByIsActive(1);
        model.addAttribute("groceries", groceriesList);
        return "view-grocery"; // Return the name of the Thymeleaf template
    }

    @GetMapping("/remove-grocery")
    public String removeGrocery(Model model) {
    	 List<Groceries> groceriesList = groceryRepository.findByIsActive(1); // Only active items
         model.addAttribute("groceries", groceriesList);

        return "remove-grocery";
    }
    
    @PostMapping("/admin/remove-grocery")
    public String removeGroceries(@RequestParam("selectedIds") List<Long> selectedIds, Model model,RedirectAttributes redirectAttributes) {
        for (Long id : selectedIds) {
            Groceries grocery = groceryRepository.findById(id);
            if (grocery != null) {
                grocery.setIsActive(0);  // Mark as inactive
                groceryRepository.save(grocery);
            }
        }
        redirectAttributes.addFlashAttribute("message_remove", "Selected groceries have been removed.");
        return "redirect:/admin/dashboard"; // Redirect to the remove grocery page
    }

    @GetMapping("/update-details")
    public String updateDetails(Model model) {
    	List<Groceries> groceriesList = groceryRepository.findByIsActive(1); // Fetch active groceries
        model.addAttribute("groceries", groceriesList);
        return "update-details"; 
    }
    
    @PostMapping("/admin/update-details")
    public String updateDetails(@RequestParam("selectedIds") List<Long> selectedIds,
                                @RequestParam("name") List<String> names,
                                @RequestParam("category") List<String> categories,
                                @RequestParam("pricePerUnit") List<Double> prices,
                                @RequestParam("quantityInStock") List<Integer> quantities,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        System.out.println("INSIDE /admin/update-details");
        // Update details for each selected row
        for (int i = 0; i < selectedIds.size(); i++) {
            Groceries grocery = groceryRepository.findById(selectedIds.get(i));
            if (grocery != null) {
                grocery.setName(names.get(i));
                grocery.setCategory(categories.get(i));
                grocery.setPricePerUnit(prices.get(i));
                grocery.setQuantityInStock(quantities.get(i));
                groceryRepository.save(grocery);
            }
        }
        redirectAttributes.addFlashAttribute("message", "Selected groceries have been updated.");
        return "redirect:/update-details"; // Redirect back to the page
    }

    @GetMapping("/user-groceries")
    public String viewGroceryList(Model model) {
        List<Groceries> groceries = groceryService.getInStockActiveGroceries();
        model.addAttribute("groceries", groceries);
        return "user-groceries";
    }
    
    @PostMapping("/user/book-groceries")
    public String bookGroceries(@RequestParam("groceryIds") List<Long> groceryIds, Model model) {
        groceryService.bookGroceries(groceryIds);
        model.addAttribute("message", "Groceries booked successfully!");
        return "redirect:/user/groceries";
    }
}
