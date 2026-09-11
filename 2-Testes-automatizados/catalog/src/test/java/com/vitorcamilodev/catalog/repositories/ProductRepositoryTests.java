package com.vitorcamilodev.catalog.repositories;

import com.vitorcamilodev.catalog.entities.Product;
import com.vitorcamilodev.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.Optional;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ProductRepositoryTests {

    private final ProductRepository repository;

    public ProductRepositoryTests(ProductRepository repository) {
        this.repository = repository;
    }

    private long existingId;
    private long notExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        notExistingId = -1L;
        countTotalProducts = repository.count();
    }

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
        Optional<Product> result = repository.findById(notExistingId);

        Assertions.assertFalse(result.isPresent());
    }
}
