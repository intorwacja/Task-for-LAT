package org.example.task_for_lat.controllers;
import org.example.task_for_lat.entity.Product;
import org.example.task_for_lat.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product findProductById(@PathVariable Long id){
        return productService.findProductByID(id);
    }

    @GetMapping("/all")
    public List<Product> getProductList(){
        return productService.getProductList();
    }

    @PostMapping("/add")
    public void addProduct(@RequestBody Product product){
        productService.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @GetMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateDiscountPrice(@RequestParam Long id, @RequestParam String code) {
        return productService.calculatePrice(id, code);
    }
}
