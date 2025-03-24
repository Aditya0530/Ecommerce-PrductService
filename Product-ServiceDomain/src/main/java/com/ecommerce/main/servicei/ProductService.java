package com.ecommerce.main.servicei;


import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.main.dto.ProductDto;
import com.ecommerce.main.model.Product;

public interface ProductService {
public ProductDto saveProduct(String productJson,MultipartFile file);

public Iterable<Product> getAll();

public Product getById(int productId);

public void deleteById(int productId);

}
