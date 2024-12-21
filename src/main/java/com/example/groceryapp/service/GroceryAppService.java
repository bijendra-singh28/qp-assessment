package com.example.groceryapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.groceryapp.model.Groceries;

@Service
public interface GroceryAppService {

	public List<Groceries> getActiveGroceries();
	public List<Groceries> getInStockActiveGroceries();
	public void bookGroceries(List<Long> groceryIds);
}
