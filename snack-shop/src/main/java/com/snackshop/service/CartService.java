package com.snackshop.service;

import com.snackshop.model.CartItem;
import com.snackshop.model.Product;
import com.snackshop.repository.CartRepository;
import com.snackshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public List<CartItem> getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(String userId, String productId, Integer quantity) {
        // 检查商品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 检查库存
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        // 检查购物车中是否已有该商品
        Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingItem.isPresent()) {
            // 更新数量
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setUpdatedAt(LocalDateTime.now());
            return cartRepository.save(item);
        } else {
            // 添加新商品
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setProductName(product.getName());
            newItem.setProductImage(product.getImageUrl());
            newItem.setProductPrice(product.getPrice());
            return cartRepository.save(newItem);
        }
    }

    public CartItem updateCartItem(String userId, String productId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(userId, productId);
            return null;
        }

        // 检查商品库存
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        CartItem item = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("购物车中不存在该商品"));

        item.setQuantity(quantity);
        item.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(item);
    }

    public void removeFromCart(String userId, String productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }

    public long getCartCount(String userId) {
        return cartRepository.countByUserId(userId);
    }

    public double calculateCartTotal(String userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }
}