package com.example.groceryapp.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.groceryapp.model.Groceries;

@Repository
public interface GroceriesRepository extends JpaRepository<Groceries,BigInteger> {


    List<Groceries> findByIsActive(int isActive);
    
    Groceries findById(Long id);

    @Query("SELECT g FROM Groceries g WHERE g.id IN :ids")
    List<Groceries> findGroceriesByIds(@Param("ids") List<Long> ids);

    @Query("SELECT g FROM Groceries g WHERE g.id IN :gid")
    Groceries findGroceriesById(@Param("gid") Long gid);
    

    @Query("SELECT g FROM Groceries g WHERE g.isActive = 1 AND g.quantityInStock > 0")
    List<Groceries> findActiveGrocery();
    
}
