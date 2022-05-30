package com.example.projetoestagio.controller;

import com.example.projetoestagio.exception.NotFoundException;
import com.example.projetoestagio.model.Product;
import com.example.projetoestagio.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    ProductController productController;


    private MockMvc mockMvc;

    @Mock
    ProductService productService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .build();
    }

    @Test
    @SneakyThrows
    void deveListarTodosOsProdutos() {
        URI uri = new URI("/products");

        Mockito.when(productService.findAll()).thenReturn(this.mockListProduct());

        mockMvc.perform(MockMvcRequestBuilders
                .get(uri)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @SneakyThrows
    void deveBuscarUmProdutoPorId() {
        URI uri = new URI("/products/1");

        Mockito.when(productService.findById(Mockito.anyInt())).thenReturn(Optional.of(this.mockProduct(1)));


        mockMvc.perform(MockMvcRequestBuilders
                .get(uri)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    @SneakyThrows
    void deveCriarUmProduto() {
        URI uri = new URI("/products");

        Product product = this.mockProduct(1);

        String content = objectMapper.writeValueAsString(product);

        Mockito.when(productService.save(Mockito.any())).thenReturn((this.mockProduct(1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(content));
    }


    @Test
    @SneakyThrows
    void deveAtualizarUmProduto() {

        URI uri = new URI("/products/1");

        Product product = this.mockProduct(1);


        Mockito.when(productService.findById(Mockito.anyInt())).thenReturn(Optional.of(product));

        String content = objectMapper.writeValueAsString(product);

        Mockito.when(productService.save(product)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(content));

    }

    @Test
    @SneakyThrows
    void deveRetornar404AoAtualizarUmProdutoComIdInvalido() {

        URI uri = new URI("/products/1");

        Product product = this.mockProduct(1);

        Mockito.when(productService.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        String content = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

    }

    @Test
    @SneakyThrows
    void deveDeletarUmProduto() {

        URI uri = new URI("/products/1");

        mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    void deveRetornar404AoDeletarProdutoComIdInvalido() {
        URI uri = new URI("/products/1");

        Mockito.doThrow(NotFoundException.class)
                        .when(productService)
                        .deleteById(1);

        mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    @SneakyThrows
    void deveBuscarProdutos() {

        URI uri = new URI("/products/search");

        Mockito.when(productService.searchBy(Mockito.anyString(),Mockito.anyDouble(),Mockito.anyDouble())).thenReturn(this.mockListProduct());

        mockMvc.perform(MockMvcRequestBuilders
                .get(uri)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }

    private List<Product> mockListProduct() {
        ArrayList<Product> produtos = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            produtos.add(mockProduct(i));
        }
        return produtos;
    }

    private Product mockProduct(int id) {
        return new Product(id, "Description", "produto " + id, 10 * id);
    }


}