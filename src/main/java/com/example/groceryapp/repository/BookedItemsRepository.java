package com.example.groceryapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.groceryapp.model.BookedItem;

@Repository
public interface BookedItemsRepository extends JpaRepository<BookedItem, Long>  {

    List<BookedItem> findByBookedBy(String bookedBy);

}
