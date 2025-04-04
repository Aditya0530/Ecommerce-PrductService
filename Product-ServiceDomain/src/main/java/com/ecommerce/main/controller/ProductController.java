package com.ecommerce.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.main.dto.ProductDto;
import com.ecommerce.main.model.Product;
import com.ecommerce.main.servicei.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping("/postProduct")
	public ResponseEntity<ProductDto> saveProduct(@RequestPart("product") String p,
			@RequestPart("productImage") List<MultipartFile> file) {
		ProductDto pr = productService.saveProduct(p, file);
		return new ResponseEntity<>(pr, HttpStatus.CREATED);
	}

	@GetMapping("/getAll")
	public ResponseEntity<Iterable<Product>> getData() {
		Iterable<Product> p = productService.getAll();
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	@GetMapping("/getById/{productId}")
	public ResponseEntity<Product> getById(@PathVariable("productId") int productId) {
		Product p = productService.getById(productId);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	@DeleteMapping("/deleteById/{productId}")
	public ResponseEntity<String> deleteData(@PathVariable("productId") int productId) {
		productService.deleteById(productId);
		return new ResponseEntity<>("Data Deleted Successfully", HttpStatus.OK);
	}

	// patch 1 productid as reference 2 flagAvailable
	@PatchMapping("/updateFlag/{available}/{productId}")
	public ResponseEntity<?> partialUpdate(@PathVariable("available") boolean isAvailable,
			@PathVariable("productId") int productId) {
		productService.patchProduct(isAvailable, productId);
		return new ResponseEntity<>("Partially Updated Data", HttpStatus.OK);
	}

	// updateQuantity by productId
	@PatchMapping("/updateQuantity/{quantity}/{productId}")
	public ResponseEntity<?> quantityUpdate(@PathVariable("quantity") int available,
			@PathVariable("productId") int productId) {
		productService.quantityAvailable(available, productId);
		return new ResponseEntity<>("Partially Updated Data", HttpStatus.OK);
	}
	
	@GetMapping("/getByName/{productName}")
	public ResponseEntity<Iterable<Product>> getproductByname(@PathVariable("productName") String productName) {
		Iterable<Product> p = productService.getByName(productName);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}
	
}
