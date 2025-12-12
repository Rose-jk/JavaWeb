package com.snackshop.service;

import com.snackshop.model.Product;
import com.snackshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public List<Product> getFeaturedProducts(int limit) {
        List<Product> allProducts = productRepository.findByActiveTrue();
        return allProducts.stream()
                .sorted((p1, p2) -> Double.compare(p2.getSales(), p1.getSales()))
                .limit(limit)
                .toList();
    }

    public Product createProduct(Product product) {
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        product.setActive(true);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setFullDescription(product.getFullDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setOriginalPrice(product.getOriginalPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setStock(product.getStock());
        existingProduct.setTags(product.getTags());
        existingProduct.setWeight(product.getWeight());
        existingProduct.setExpiryDate(product.getExpiryDate());
        existingProduct.setOrigin(product.getOrigin());
        existingProduct.setStorage(product.getStorage());
        existingProduct.setUpdateTime(LocalDateTime.now());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setActive(false);
        productRepository.save(product);
    }
}