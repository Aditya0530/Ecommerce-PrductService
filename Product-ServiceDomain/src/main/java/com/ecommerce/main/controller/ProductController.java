package com.ecommerce.main.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	//postAll
	@PostMapping("/postProduct")

	public ResponseEntity<List<ProductDto>> saveProduct(@RequestPart("product") String p,

			@RequestPart("productImage") List<MultipartFile> file) {
		List<ProductDto> pr = pi.saveProduct(p, file);
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
	@DeleteMapping("/deleteById/{productId}")
	public ResponseEntity<String> deleteData(@PathVariable("productId")int productId){
	 pi.deleteById(productId);	
	   return new ResponseEntity<>("Data Deleted Successfully",HttpStatus.OK);
	}

	//patch 1 productid as reference 2 flagAvailable
	@PatchMapping("/updateFlag/{available}/{productId}")
	public ResponseEntity<?> partialUpdate(@PathVariable("available") boolean isAvailable,
			@PathVariable("productId") int productId) {
		pi.patchProduct(isAvailable,productId);
		return new ResponseEntity<>("Partially Updated Data", HttpStatus.OK);
	}


}
