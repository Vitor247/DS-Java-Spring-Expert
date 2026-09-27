package com.vitorcamilodev.catalog.services;

import com.vitorcamilodev.catalog.repositories.ProductRepository;
import com.vitorcamilodev.catalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 247L;
    }

    @Test
    void deleteShouldDeleteResourceWhenIdExists() {
        Assertions.assertTrue(productRepository.existsById(existingId));

        productService.delete(existingId);

        Assertions.assertFalse(productRepository.existsById(existingId));
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }
}