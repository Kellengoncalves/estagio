package com.example.projetoestagio.repository;

import com.example.projetoestagio.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository< Product,Integer>, JpaSpecificationExecutor<Product> {

}
