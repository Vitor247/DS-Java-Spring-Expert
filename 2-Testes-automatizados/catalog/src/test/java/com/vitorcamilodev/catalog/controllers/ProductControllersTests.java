package com.vitorcamilodev.catalog.controllers;

import com.vitorcamilodev.catalog.controller.ProductController;
import com.vitorcamilodev.catalog.dto.ProductDTO;
import com.vitorcamilodev.catalog.services.ProductService;
import com.vitorcamilodev.catalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllersTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() {
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        mockMvc.perform(get("/products")).andExpect(status().isOk());
    }


}
