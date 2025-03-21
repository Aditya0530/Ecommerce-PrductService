package com.adi.main.controller;


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

import com.adi.main.dto.ErrorDto;
import com.adi.main.exceptions.ProductException;
import com.adi.main.model.Product;
import com.adi.main.servicei.ProductService;
import com.adi.main.serviceimpl.ProductServiceImpl;

import jakarta.validation.Valid;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService pi;
	
	 private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	
	@PostMapping("/testException")
	public ResponseEntity<String> testExceptionHandling() {
	    throw new ProductException("This is a test exception!");
	}
	@PostMapping("/postProduct")
	public ResponseEntity<Product> saveProduct (@RequestPart(value = "product", required = false) String productJson, // Now optional
	        @RequestPart(value = "productImage", required = false) MultipartFile file) { // Now optional

	    // Manually check for missing product JSON
	    if (productJson == null || productJson.trim().isEmpty()) {
	        LOG.error("Invalid request: Product JSON is missing or empty.");
	        throw new ProductException("Product JSON and product image are required. Please provide valid data.");
	    }

	    try {
	        Product pr = pi.saveProduct(productJson, file);
	        return new ResponseEntity<>(pr, HttpStatus.CREATED);
	    } catch (ProductException ex) {
	        LOG.error("Exception caught: {}", ex.getMessage());
	        throw ex;
	    }
		
	}
		
		@GetMapping("/getAll")
		public ResponseEntity<Iterable<Product>> getData(){
			Iterable<Product> p=pi.getAll();
			
			return new ResponseEntity<>(p,HttpStatus.OK);
		}
	
		@GetMapping("/getById/{productId}")
		public ResponseEntity<Product> getById(@PathVariable("productId") int productId){
			Product p=pi.getById(productId);
			
			return new ResponseEntity<>(p,HttpStatus.OK);
		}
	}

