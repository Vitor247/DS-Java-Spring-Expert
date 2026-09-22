package com.vitorcamilodev.catalog.controllers;

import com.vitorcamilodev.catalog.controller.ProductController;
import com.vitorcamilodev.catalog.dto.ProductDTO;
import com.vitorcamilodev.catalog.services.ProductService;
import com.vitorcamilodev.catalog.services.exceptions.ResourceNotFoundException;
import com.vitorcamilodev.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllersTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private Long existingId;
    private Long nonExistingId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        ResultActions resultActions = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        when(service.findById(existingId)).thenReturn(productDTO);

        ResultActions resultActions = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

}
