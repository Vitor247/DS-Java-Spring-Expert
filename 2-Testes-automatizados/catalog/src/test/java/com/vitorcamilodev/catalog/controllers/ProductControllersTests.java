package com.vitorcamilodev.catalog.controllers;

import com.vitorcamilodev.catalog.controller.ProductController;
import com.vitorcamilodev.catalog.dto.ProductDTO;
import com.vitorcamilodev.catalog.services.ProductService;
import com.vitorcamilodev.catalog.services.exceptions.DatabaseException;
import com.vitorcamilodev.catalog.services.exceptions.ResourceNotFoundException;
import com.vitorcamilodev.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllersTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        when(service.findAllPaged(any())).thenReturn(page);

        ResultActions resultActions = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        when(service.findById(existingId)).thenReturn(productDTO);

        ResultActions resultActions = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        when(service.update(eq(existingId), any())).thenReturn(productDTO);

        String json = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        String json = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void insertShouldReturnProductDTOCreated() throws Exception {
        when(service.insert(any())).thenReturn(productDTO);

        String json = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(post("/products")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(service).delete(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);

        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(DatabaseException.class).when(service).delete(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest());
    }

}
