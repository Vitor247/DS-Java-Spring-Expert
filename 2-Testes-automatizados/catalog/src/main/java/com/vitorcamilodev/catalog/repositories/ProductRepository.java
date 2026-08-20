package com.vitorcamilodev.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitorcamilodev.catalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
