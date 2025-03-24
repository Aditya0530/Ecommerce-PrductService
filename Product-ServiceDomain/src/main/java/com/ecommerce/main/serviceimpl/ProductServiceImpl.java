package com.ecommerce.main.serviceimpl;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.main.dto.ProductDto;
import com.ecommerce.main.exceptions.ProductException;
import com.ecommerce.main.exceptions.ProductNotSavedException;
import com.ecommerce.main.model.Product;
import com.ecommerce.main.repository.ProductRepository;
import com.ecommerce.main.servicei.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository pr;

	@Autowired
	private ObjectMapper obj;

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public ProductDto saveProduct(String productJson, MultipartFile file) {
		LOG.info("Received request to save product: {}", productJson);

		if (productJson == null || productJson.trim().isEmpty()) {
			LOG.error("Invalid product JSON: JSON is null or empty");
			throw new ProductException("Product JSON cannot be null or empty");
		}

		Product p;
		try {
			p = obj.readValue(productJson, Product.class);
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing JSON: {}", e.getMessage(), e);
			throw new ProductException("Invalid JSON format: " + e.getMessage());
		}

		if (p.getProductId() == 0) {
			LOG.error("Product ID is missing or invalid: {}", p.getProductId());
			throw new ProductException("Product ID must be provided and valid.");
		}
		if (file == null || file.isEmpty()) {
			LOG.error("Product image file is missing or empty.");
			throw new ProductException("Product image file must be provided.");
		}

		if (file != null && !file.isEmpty()) {
			try {
				p.setProductImage(file.getBytes());
			} catch (IOException e) {
				LOG.error("Error processing the file: {}", e.getMessage());
				throw new ProductException("Failed to process product image.");
			}
		}
		Product savedProduct = pr.save(p);
		LOG.info("Product saved successfully to Database: {}", savedProduct.getProductId());
		return new ProductDto(savedProduct);

	}

	public Iterable<Product> getAll() {

		return pr.findAll();
	}

	@Override
	public Product getById(int productId) {
		return pr.getById(productId);
	}

	@Override
	public void deleteById(int productId) {
    pr.deleteById(productId);		
	}
}
