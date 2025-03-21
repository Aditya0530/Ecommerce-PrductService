package com.ecommerce.main.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	private int productId;
	@NotBlank(message = "Product name cannot be empty")

	@Size(max = 100, message = "Product name must not exceed 100 characters")
	private String productName;

	@NotBlank(message = "Description cannot be empty")
	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;

	@NotBlank(message = "Brand cannot be empty")
	@Size(max = 50, message = "Brand name must not exceed 50 characters")
	private String brand;

	@NotBlank(message = "Category cannot be empty")
	@Size(max = 50, message = "Category must not exceed 50 characters")
	private String category;

	@Positive(message = "Price must be greater than zero")
	private double price;

	@PositiveOrZero(message = "Quantity available cannot be negative")
	private int quantityAvailable;

	@NotBlank(message = "Supplier name cannot be empty")
	@Size(max = 100, message = "Supplier name must not exceed 100 characters")
	private String supplierName;

	@NotBlank(message = "Supplier contact cannot be empty")
	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid supplier contact number")
	private String supplierContact;

	@Lob
	@Column(length = 999999999)
	private byte[] productImage;

	@PositiveOrZero(message = "Warranty period cannot be negative")
	private int warrantyPeriod;
}


