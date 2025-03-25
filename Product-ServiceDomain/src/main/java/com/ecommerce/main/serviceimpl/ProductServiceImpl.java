package com.ecommerce.main.serviceimpl;

import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.main.dto.ProductDto;
import com.ecommerce.main.enums.Features;
import com.ecommerce.main.exceptions.ProductException;
import com.ecommerce.main.exceptions.ProductNotSavedException;
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
import jakarta.validation.ValidationException;
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
	private ObjectMapper obj;

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final Validator validator = null;

	@Override
	public List<ProductDto> saveProduct(String productJson, List<MultipartFile> files) {
		LOG.info("Received request to save product: {}", productJson);

		if (productJson == null || productJson.trim().isEmpty()) {
			LOG.error("Invalid product JSON: JSON is null or empty");
			throw new ProductException("Product JSON cannot be null or empty");
		}

		List<Product> products;
		try {
			products = obj.readValue(productJson, new TypeReference<List<Product>>() {
			});
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing JSON: {}", e.getMessage(), e);
			throw new ProductException("Invalid JSON format: " + e.getMessage());
		}

		if (products.isEmpty()) {
			LOG.error("Product list is empty or invalid");
			throw new ProductException("At least one product must be provided.");
		}

		if (files == null || files.isEmpty()) {
			LOG.error("Product image files are missing or empty.");
			throw new ProductException("At least one product image file must be provided.");
		}

		try {
			for (Product product : products) {

				List<ProductImage> productImages = new ArrayList<>();
				for (MultipartFile file : files) {
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
			}
		} catch (IOException e) {
			LOG.error("Error processing the file: {}", e.getMessage(), e);
			throw new ProductException("Failed to process product images.");
		}
		List<Product> savedProducts = (List<Product>) pr.saveAll(products);
		LOG.info("Products saved successfully to Database: {}", savedProducts.size());
		return savedProducts.stream().map(ProductDto::new).toList();
	}

		private void validateUser(Product p) {
		Set<ConstraintViolation<Product>> violations = validator.validate(p);
		if(!violations.isEmpty()) {
			Map<String,String> errors=new HashMap<>();
			for(ConstraintViolation<Product> violation:violations) {
				errors.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			throw new ValidationException();
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

	@Override
	public void deleteById(int productId) {
		pr.deleteById(productId);
	}
}
