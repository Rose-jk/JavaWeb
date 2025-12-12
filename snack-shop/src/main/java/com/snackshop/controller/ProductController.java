package com.snackshop.controller;

import com.snackshop.dto.ApiResponse;
import com.snackshop.model.Product;
import com.snackshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(p -> ResponseEntity.ok(ApiResponse.success(p)))
                .orElse(ResponseEntity.ok(ApiResponse.error("商品不存在")));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<Product>>> getFeaturedProducts(@RequestParam(defaultValue = "4") int limit) {
        List<Product> products = productService.getFeaturedProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getCategories() {
        List<String> categories = List.of(
                "薯片膨化", "饼干糕点", "糖果巧克力",
                "坚果炒货", "肉干肉脯", "进口零食",
                "饮料冲调", "方便速食", "健康零食"
        );
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}