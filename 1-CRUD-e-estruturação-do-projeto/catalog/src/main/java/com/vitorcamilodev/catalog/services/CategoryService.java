package com.vitorcamilodev.catalog.services;

import com.vitorcamilodev.catalog.dto.CategoryDTO;
import com.vitorcamilodev.catalog.entities.Category;
import com.vitorcamilodev.catalog.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).toList();
    }
}
