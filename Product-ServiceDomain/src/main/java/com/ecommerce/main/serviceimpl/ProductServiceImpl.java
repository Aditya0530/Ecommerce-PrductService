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
import com.ecommerce.main.enums.Features;
import com.ecommerce.main.exceptions.ProductException;
import com.ecommerce.main.exceptions.ProductNotSavedException;
import com.ecommerce.main.exceptions.ValidationException;
import com.ecommerce.main.model.Product;
import com.ecommerce.main.model.ProductFeatures;
import com.ecommerce.main.model.ProductImage;
import com.ecommerce.main.repository.ProductRepository;
import com.ecommerce.main.servicei.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository pr;
	@Autowired
	private Validator validator;
	@Autowired
	private ObjectMapper obj;

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public ProductDto saveProduct(String productJson, List<MultipartFile> files) {
		LOG.info("Received request to save product: {}", productJson);

		Product product;
		try {
			product = obj.readValue(productJson, Product.class);
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing JSON: {}", e.getMessage(), e);
			throw new ProductException("Invalid JSON format: " + e.getMessage());
		}
		validateProduct(product);
		try {
			List<ProductImage> productImages = new ArrayList<>();
			for (MultipartFile file : files) {
				if (file == null || file.isEmpty()) {
					throw new ProductException("At least one product image file must be provided.");
				}
				if (!file.isEmpty()) {
					ProductImage productImage = new ProductImage();
					productImage.setImageData(file.getBytes());
					productImages.add(productImage);
				}
			}

			product.setProductImages(productImages);

			if (product.getProductFeatures() != null && !product.getProductFeatures().isEmpty()) {
				for (int i = 0; i < product.getProductFeatures().size(); i++) {
					ProductFeatures feature = product.getProductFeatures().get(i);
					if (feature.getFeature() == null) {
						feature.setFeature(Features.RAM);
					}
				}
			}
		} catch (IOException e) {
			LOG.error("Error processing the file: {}", e.getMessage(), e);
			throw new ProductException("Failed to process product images.");
		}

		Product savedProducts = pr.save(product);
		LOG.info("Products saved successfully to Database: {}", savedProducts);

		return new ProductDto(product);
	}

	private void validateProduct(Product product) {
		Set<ConstraintViolation<Product>> violations = validator.validate(product);
		if (!violations.isEmpty()) {
			Map<String, String> errors = new HashMap<>();
			for (ConstraintViolation<Product> violation : violations) {
				errors.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			LOG.error("Product validation failed: {}", errors);
			throw new ValidationException(errors);
		}
	}

	public Iterable<Product> getAll() {

		return pr.findAll();
	}

	@Override
	public Product getById(int productId) {
		return pr.getById(productId);
	}

	@Override
	public void patchProduct(boolean isAvailable, int productId) {
		Product p = pr.getById(productId);
		if (p == null) {
			throw new ProductNotSavedException("Id Not Found For Partial Update...!");
		}
		pr.patchUpdate(isAvailable, productId);
		LOG.info("Partial Update Successfull To Database...{}");

	}

	public void deleteById(int productId) {
		pr.deleteById(productId);
	}

	@Override
	public void quantityAvailable(int quantity, int productId) {
		Product p = pr.getById(productId);
		if (p == null) {
			throw new ProductNotSavedException("Id Not Found For Partial Update...!");
		}
		pr.quantityUpdate(quantity, productId);
		LOG.info("Partial Update Successfull To Database...{}");

	}

}
