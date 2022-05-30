package com.example.projetoestagio.service;

import com.example.projetoestagio.exception.NotFoundException;
import com.example.projetoestagio.model.Product;
import com.example.projetoestagio.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;


    @BeforeEach
    private void beforeEach() {
        this.productService = new ProductService(productRepository);
    }


    @Test
    void deveBuscarTodosProdutos() {

        var productList = this.mockListProduct();

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        assertEquals(productList, productService.findAll());

    }

    @Test
    void deveBuscarUmProdutoPorId() {
        var product = this.mockProduct(1);

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertEquals(productService.findById(1).get().getId(), product.getId());

    }

    @Test
    void deveSalvarUmProduto() {
        var product = this.mockProduct(1);

        productService.save(product);

        Mockito.verify(productRepository).save(product);
    }

    @Test
    void deveDeletarUmProdutoPorId() {

        productService.deleteById(1);

        Mockito.verify(productRepository).deleteById(1);

    }

    @Test
    void deveFalharDeletarUmProdutoComIdInvalido() {

        Mockito.doThrow(EmptyResultDataAccessException.class)
                .when(productRepository).deleteById(Mockito.any());

        assertThrows(NotFoundException.class,
                () -> productService.deleteById(1));

    }


    @Test
    void searchBy() {
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