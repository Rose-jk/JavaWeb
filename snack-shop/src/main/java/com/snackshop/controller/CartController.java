package com.snackshop.controller;

import com.snackshop.dto.ApiResponse;
import com.snackshop.model.CartItem;
import com.snackshop.model.User;
import com.snackshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@AuthenticationPrincipal User user) {
        List<CartItem> cartItems = cartService.getCartByUserId(user.getId());
        double total = cartService.calculateCartTotal(user.getId());
        long count = cartService.getCartCount(user.getId());

        CartResponse response = new CartResponse();
        response.setItems(cartItems);
        response.setTotal(total);
        response.setCount(count);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @AuthenticationPrincipal User user,
            @RequestBody AddToCartRequest request) {
        CartItem item = cartService.addToCart(
                user.getId(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(ApiResponse.success("商品已添加到购物车", item));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateCartRequest request) {
        CartItem item = cartService.updateCartItem(
                user.getId(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(ApiResponse.success("购物车已更新", item));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(
            @AuthenticationPrincipal User user,
            @PathVariable String productId) {
        cartService.removeFromCart(user.getId(), productId);
        return ResponseEntity.ok(ApiResponse.success("商品已从购物车移除", null));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.ok(ApiResponse.success("购物车已清空", null));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getCartCount(@AuthenticationPrincipal User user) {
        long count = cartService.getCartCount(user.getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Double>> getCartTotal(@AuthenticationPrincipal User user) {
        double total = cartService.calculateCartTotal(user.getId());
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    // 请求和响应类
    @Data
    public static class AddToCartRequest {
        private String productId;
        private Integer quantity = 1;
    }

    @Data
    public static class UpdateCartRequest {
        private String productId;
        private Integer quantity;
    }

    @Data
    public static class CartResponse {
        private List<CartItem> items;
        private double total;
        private long count;
    }
}