package com.adi.main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adi.main.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{

	
}
