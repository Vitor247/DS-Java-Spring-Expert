package com.vitorcamilodev.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitorcamilodev.catalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
