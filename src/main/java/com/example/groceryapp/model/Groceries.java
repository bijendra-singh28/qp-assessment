package com.example.groceryapp.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="groceries")
@NamedQuery(name="Groceries.findAll", query="SELECT e FROM Groceries e")
public class Groceries  implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private BigInteger id;

	@Column(name="name")
	private String name;

	@Column(name="category")
	private String category;

	@Column(name="price_per_unit")
	private Double pricePerUnit;

	@Column(name="quantity_in_stock")
	private Integer quantityInStock;

	@Column(name="is_perishable")
	private Boolean isPerishable;

	@Column(name="unit")
	private String unit;

	@Column(name="expiry_date")
	private LocalDate expiryDate;

	@Column(name="date_added")
	private LocalDate date_added;
	
	@Column(name="is_active")
	private int isActive;
	
}
