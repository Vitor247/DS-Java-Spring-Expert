package com.vitorcamilodev.catalog.services;

import com.vitorcamilodev.catalog.entities.Category;
import com.vitorcamilodev.catalog.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll(){
        return repository.findAll();
    }
}
