package com.example.groceryapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.groceryapp.model.Groceries;
import com.example.groceryapp.repository.GroceriesRepository;

@Service("GroceryAppService")
public class GroceryAppServiceImpl implements GroceryAppService {

	@Autowired
	GroceriesRepository groceriesRepository;
	
	@Override
	public List<Groceries> getActiveGroceries() {
	        return groceriesRepository.findByIsActive(1);
	}
	
	@Override
	public List<Groceries> getInStockActiveGroceries() {
	        return groceriesRepository.findActiveGrocery();
	}

	@Override
	public void bookGroceries(List<Long> groceryIds) {
	        // Logic to handle the booking (e.g., reduce stock)
	        List<Groceries> groceriesToBook = groceriesRepository.findGroceriesByIds(groceryIds);
	        groceriesToBook.forEach(grocery -> {
	            if (grocery.getQuantityInStock() > 0) {
	                grocery.setQuantityInStock(grocery.getQuantityInStock() - 1); // Decrease stock
	            }
	        });
	        groceriesRepository.saveAll(groceriesToBook);
	}
}
