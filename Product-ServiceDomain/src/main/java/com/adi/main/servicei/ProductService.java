package com.adi.main.servicei;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.adi.main.dto.ProductDto;
import com.adi.main.model.Product;

public interface ProductService {
public Product saveProduct(String productJson,MultipartFile file);
}
