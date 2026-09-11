package com.vitorcamilodev.catalog.services;

import com.vitorcamilodev.catalog.dto.ProductDTO;
import com.vitorcamilodev.catalog.entities.Category;
import com.vitorcamilodev.catalog.entities.Product;
import com.vitorcamilodev.catalog.repositories.CategoryRepository;
import com.vitorcamilodev.catalog.repositories.ProductRepository;
import com.vitorcamilodev.catalog.services.exceptions.DatabaseException;
import com.vitorcamilodev.catalog.services.exceptions.ResourceNotFoundException;
import com.vitorcamilodev.catalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() {
        when(repository.findById(existingId)).thenReturn(Optional.of(product));

        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
    }

    @Test
    void findAllPagedShouldReturnPage() {
        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);

        verify(repository).findAll(pageable);
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() {
        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, productDTO));
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        when(repository.existsById(existingId)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> service.delete(existingId));

        verify(repository).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        when(repository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));

        verify(repository, never()).deleteById(nonExistingId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        when(repository.existsById(dependentId)).thenReturn(true);

        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> service.delete(dependentId));
    }
}
