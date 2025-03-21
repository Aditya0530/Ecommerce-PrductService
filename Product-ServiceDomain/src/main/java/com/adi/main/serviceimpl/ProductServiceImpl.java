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
		
		    //  Check for missing or empty product JSON before parsing
		    if (productJson == null || productJson.trim().isEmpty()) {
		        LOG.error("Invalid product data: Product JSON or product image cannot be null or empty");
		        throw new ProductException("Product JSON and product image are required. Please provide valid data.");
		    }

		     p = null; // Ensure initialization

		    try {
		        //  Try parsing JSON and handle malformed JSON
		        p = obj.readValue(productJson, Product.class);

		        // Check if product JSON parsing resulted in a null object
		        if (p == null) {
		            LOG.error("Error: Parsed Product object is null.");
		            throw new ProductException("Invalid product JSON format. Please provide valid data.");
		        }

		        //  Check if product name is missing or empty
		        if (p.getProductName() == null || p.getProductName().trim().isEmpty()) {
		            LOG.error("Invalid product data: Product name cannot be empty");
		            throw new ProductException("Product name is required.");
		        }

		        //  Check if image is missing
		        if (file == null || file.isEmpty()) {
		            throw new ProductNotSavedException("Uploaded file is empty. Please upload a valid image.");
		        }

		        //  Check if file is an image
		        String fileName = file.getOriginalFilename();
		        if (fileName == null || !isImageFile(fileName)) {
		            LOG.error("Invalid file type: {}", fileName);
		            throw new ProductNotSavedException("Invalid file type. Please upload an image file (JPG, JPEG, PNG, GIF).");
		        }

		        // Process the image
		        byte[] imageData = file.getBytes();
		        if (imageData.length == 0) {
		            LOG.error("Uploaded file is empty (0 bytes).");
		            throw new ProductNotSavedException("Uploaded file is empty. Please upload a valid image.");
		        }
		        p.setProductImage(imageData);

		        //  Save the product
		        p = pr.save(p);
		        LOG.info("Product saved successfully: {}", p.getProductId());

		        return p;

		    }catch (JsonProcessingException e) {
		        LOG.error("Invalid product JSON format: {}", e.getMessage());
		        throw new ProductException("Invalid product JSON format. Please provide valid data.");
		    } catch (IOException e) {
		        LOG.error("File processing error: {}", e.getMessage());
		        throw new ProductException("Failed to process product image.");
		    } catch (Exception e) {
		        LOG.error("Unexpected error occurred: {}", e.getMessage(), e);
		        throw new ProductNotSavedException("Unexpected error occurred: " + e.getMessage());
		    }
	}
	/**
	 * Method to check if the uploaded file is an image.
	 */
	private boolean isImageFile(String fileName) {
	    String lowerCaseFileName = fileName.toLowerCase();
	    return lowerCaseFileName.endsWith(".jpg") || 
	           lowerCaseFileName.endsWith(".jpeg") || 
	           lowerCaseFileName.endsWith(".png") || 
	           lowerCaseFileName.endsWith(".gif") || 
	           lowerCaseFileName.endsWith(".bmp");
	}
}

