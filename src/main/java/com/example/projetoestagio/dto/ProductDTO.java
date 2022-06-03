package com.example.projetoestagio.dto;

import com.example.projetoestagio.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Integer id;
    private String description;
    
    @NotBlank(message = "Name is mandatory")
    private String name;

    @DecimalMin(value ="0.01" ,message = "Invalid price, must be greater than zero")
    private double price;

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getDescription(),
                product.getName(),
                product.getPrice()
        );
    }
    public static List<ProductDTO> toDTO(List<Product> product) {
        return product.stream().map(ProductDTO::toDTO).collect(Collectors.toList());
    }

    public Product toProduct() {
        return new Product(
                this.getId(),
                this.getDescription(),
                this.getName(),
                this.getPrice()
        );
    }

}
