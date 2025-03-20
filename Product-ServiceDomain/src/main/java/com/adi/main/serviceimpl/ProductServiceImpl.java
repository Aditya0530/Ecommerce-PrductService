package com.adi.main.serviceimpl;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.adi.main.exceptions.ProductException;
import com.adi.main.exceptions.ProductNotSavedException;
import com.adi.main.model.Product;
import com.adi.main.repository.ProductRepository;
import com.adi.main.servicei.ProductService;
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
	public Product saveProduct(String productJson, MultipartFile file) {
		LOG.info("Received request to save product: {}", productJson);
		Product p;

		try {

			if (productJson == null || productJson.trim().isEmpty()) {
				LOG.error("Invalid product JSON: JSON is null or empty");
				throw new ProductException("Product JSON cannot be null or empty");
			}

			p = obj.readValue(productJson, Product.class);

			if (p.getProductId() == 0) {
				LOG.error("Product ID is missing or invalid: {}", p.getProductId());
				throw new ProductException("Product ID must be provided and valid.");
			}

			if (file != null && !file.isEmpty()) {
				try {
					p.setProductImage(file.getBytes());
				} catch (IOException e) {
					LOG.error("Error processing the file: {}", e.getMessage());
					throw new ProductNotSavedException("Error processing file: " + e.getMessage());
				}
			}

			p = pr.save(p);
			LOG.info("Product saved successfully: {}", p.getProductId());

			return p;
		} catch (JsonProcessingException e) {

			LOG.error("Error parsing JSON: {}", e.getMessage(), e);
			throw new ProductException("Invalid JSON format: " + e.getMessage());
		} catch (ProductException ex) {

			LOG.error("Product exception occurred: {}", ex.getMessage());
			throw ex;
		} catch (ProductNotSavedException ex) {

			LOG.error("Product not saved: {}", ex.getMessage());
			throw ex;
		} catch (Exception e) {

			LOG.error("Unexpected error occurred: {}", e.getMessage(), e);
			throw new ProductNotSavedException("Unexpected error occurred: " + e.getMessage());
		}
	}
}
