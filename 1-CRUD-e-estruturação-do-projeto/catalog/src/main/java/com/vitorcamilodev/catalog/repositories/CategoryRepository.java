package com.vitorcamilodev.catalog.repositories;

import com.vitorcamilodev.catalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
