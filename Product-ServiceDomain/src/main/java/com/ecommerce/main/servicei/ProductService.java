package com.ecommerce.main.servicei;


import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.main.dto.ProductDto;
import com.ecommerce.main.model.Product;

public interface ProductService {
public List<ProductDto> saveProduct(String productJson,List<MultipartFile> file);

public Iterable<Product> getAll();

public Product getById(int productId);

public void patchProduct(boolean isAvailable,int productId);

public void deleteById(int productId);
}
