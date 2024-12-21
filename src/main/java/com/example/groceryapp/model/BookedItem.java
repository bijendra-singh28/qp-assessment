package com.example.groceryapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "booked_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "grocery_id", nullable = false)
//    private Long groceryId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "booked_by", nullable = false)
    private String bookedBy; // 'admin' or 'user'

    @Column(name = "booking_date", nullable = false, updatable = false)
    private Timestamp bookingDate;
    
    @ManyToOne
    @JoinColumn(name = "grocery_id", nullable = false)
    private Groceries grocery; // Reference to the Groceries entity

}
