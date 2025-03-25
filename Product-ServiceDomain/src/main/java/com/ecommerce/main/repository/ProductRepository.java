package com.ecommerce.main.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.main.model.Product;

import jakarta.transaction.Transactional;


@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.productId = :productId")
	Product getById(@Param("productId") int productId);

	@Modifying
	@Transactional
	@Query("update Product p set p.available= :available where p.productId= :productId")
	void patchUpdate(@Param("available") boolean isAvailable,@Param("productId") int productId);
	
	@Modifying
	@Transactional
	@Query("delete Product p where p.productId= :productId")
	public void deleteProduct(@Param("productId") int productId);
}
