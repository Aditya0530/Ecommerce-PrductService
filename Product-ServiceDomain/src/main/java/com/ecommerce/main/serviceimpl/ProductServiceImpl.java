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
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository pr;

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

		if (product == null) {
			LOG.error("Product is empty or invalid");
			throw new ProductException("At least one product must be provided.");
		}

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

	@Override
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

	@Override
	public Product updateProduct(int productId, String productJson, List<MultipartFile> files) {
		Product product;
		try {
			product = obj.readValue(productJson, Product.class);
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing JSON: {}", e.getMessage(), e);
			throw new ProductException("Invalid JSON format: " + e.getMessage());
		}

		Product existingProduct = pr.findById(productId).orElseThrow(() -> new ProductException("Product not found"));

		existingProduct.setProductName(product.getProductName());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setBrand(product.getBrand());
		existingProduct.setCategory(product.getCategory());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setQuantityAvailable(product.getQuantityAvailable());
		existingProduct.setSupplierName(product.getSupplierName());
		existingProduct.setSupplierContact(product.getSupplierContact());
		existingProduct.setWarrantyPeriod(product.getWarrantyPeriod());
		existingProduct.setAvailable(product.isAvailable());

		try {
			List<ProductImage> productImages = new ArrayList<>();
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					ProductImage productImage = new ProductImage();
					productImage.setImageData(file.getBytes());
					productImages.add(productImage);
				}
			}
			existingProduct.setProductImages(productImages);
		} catch (IOException e) {
			LOG.error("Error processing the file: {}", e.getMessage(), e);
			throw new ProductException("Failed to process product images.");
		}

		Product updatedProduct = pr.save(existingProduct);
		LOG.info("Product updated successfully: {}", updatedProduct);

		return updatedProduct;
	}

}
