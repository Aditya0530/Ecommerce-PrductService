package com.adi.main.servicei;


import org.springframework.web.multipart.MultipartFile;

import com.adi.main.model.Product;

public interface ProductService {
public Product saveProduct(String productJson,MultipartFile file);

public Iterable<Product> getAll();

public Product getById(int productId);
}
