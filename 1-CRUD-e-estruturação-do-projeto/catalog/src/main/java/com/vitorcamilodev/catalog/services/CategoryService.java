package com.vitorcamilodev.catalog.services;

import com.vitorcamilodev.catalog.dto.CategoryDTO;
import com.vitorcamilodev.catalog.entities.Category;
import com.vitorcamilodev.catalog.repositories.CategoryRepository;
import com.vitorcamilodev.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return new CategoryDTO(category);
    }
}
