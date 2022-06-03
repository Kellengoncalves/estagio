package com.example.projetoestagio.controller;

import com.example.projetoestagio.dto.ProductDTO;
import com.example.projetoestagio.exception.NotFoundException;
import com.example.projetoestagio.model.ExceptionResponse;
import com.example.projetoestagio.model.Product;
import com.example.projetoestagio.service.ProductService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.projetoestagio.dto.ProductDTO.toDTO;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ExceptionResponse.class),
    })
    public ResponseEntity<List<ProductDTO>> list() {
        return ResponseEntity.ok(
                toDTO(productService.findAll())
        );
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ProductDTO.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ExceptionResponse.class)
    })
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(
                toDTO(productService.save(productDTO.toProduct())),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/search")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ExceptionResponse.class),
    })
    public ResponseEntity<List<ProductDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Double max_price,
            @RequestParam(required = false) Double min_price
    ) {
        return ResponseEntity.ok(toDTO(productService.searchBy(q, min_price, max_price)));
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ExceptionResponse.class),
            @ApiResponse(code = 404, message = "Product not found", response = ExceptionResponse.class)
    })
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                toDTO(productService.findById(id)
                        .orElseThrow(NotFoundException::new)
                ));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ExceptionResponse.class),
            @ApiResponse(code = 404, message = "Product not found", response = ExceptionResponse.class)
    })
    public ResponseEntity<ProductDTO> update(@RequestBody @Valid ProductDTO dto, @PathVariable Integer id) {
        Product product = productService.findById(id)
                .map(p -> {
                    p.setPrice(dto.getPrice());
                    p.setName(dto.getName());
                    p.setDescription(dto.getDescription());
                    return p;
                }).orElseThrow(NotFoundException::new);

        return ResponseEntity.ok(toDTO(productService.save(product)));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Unauthorized", response = ExceptionResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = ExceptionResponse.class),
            @ApiResponse(code = 404, message = "Product not found", response = ExceptionResponse.class)
    })
    public ResponseEntity<ProductDTO> delete(@PathVariable Integer id) {

        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
