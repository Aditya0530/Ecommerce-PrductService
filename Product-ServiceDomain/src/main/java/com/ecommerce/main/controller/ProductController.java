package com.ecommerce.main.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.main.dto.ErrorDto;
import com.ecommerce.main.dto.ProductDto;
import com.ecommerce.main.exceptions.ProductException;
import com.ecommerce.main.model.Product;
import com.ecommerce.main.servicei.ProductService;
import com.ecommerce.main.serviceimpl.ProductServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService pi;

	@PostMapping("/postProduct")
	public ResponseEntity<ProductDto> saveProduct(@RequestPart("product") String p,
			@RequestPart("productImage") MultipartFile file) {
		ProductDto pr = pi.saveProduct(p, file);
		return new ResponseEntity<>(pr, HttpStatus.CREATED);

	}

	@GetMapping("/getAll")
	public ResponseEntity<Iterable<Product>> getData() {
		Iterable<Product> p = pi.getAll();
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	@GetMapping("/getById/{productId}")
	public ResponseEntity<Product> getById(@PathVariable("productId") int productId) {
		Product p = pi.getById(productId);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}
}
