package com.example.projetoestagio.service;

import com.example.projetoestagio.exception.NotFoundException;
import com.example.projetoestagio.model.Product;
import com.example.projetoestagio.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
         return productRepository.findById(id);
    }


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Integer id) {
        try {
        productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException();
        }

    }

    public List<Product> searchBy(String name, Double minPrice, Double maxPrice) {
        return productRepository.findAll(Specification.where(
                withName(name)
                        .and(withMinimalPrice(minPrice))
                        .and(withMaxPrice(maxPrice))
        ));
    }

    private static Specification<Product> withName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            final String sName = name.toUpperCase();

            return criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + sName + "%");
        };
    }

    private static Specification<Product> withMinimalPrice(Double minimalPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minimalPrice == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minimalPrice);
        };
    }

    private static Specification<Product> withMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

}
