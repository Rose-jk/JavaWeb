package com.snackshop.controller;

import com.snackshop.dto.ApiResponse;
import com.snackshop.model.Order;
import com.snackshop.model.User;
import com.snackshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String status) {
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getUserOrdersByStatus(user.getId(), status);
        } else {
            orders = orderService.getUserOrders(user.getId());
        }
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        Order order = orderService.getOrderById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderService.CreateOrderRequest request) {
        Order order = orderService.createOrder(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("订单创建成功", order));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        Order order = orderService.cancelOrder(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("订单已取消", order));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<Order>> payOrder(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        Order order = orderService.payOrder(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("支付成功", order));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<Order>> completeOrder(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        Order order = orderService.completeOrder(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("订单已完成", order));
    }

    @GetMapping("/status/counts")
    public ResponseEntity<ApiResponse<OrderStatusCounts>> getOrderStatusCounts(
            @AuthenticationPrincipal User user) {
        List<Order> allOrders = orderService.getUserOrders(user.getId());

        OrderStatusCounts counts = new OrderStatusCounts();
        counts.setPending((int) allOrders.stream().filter(o -> "pending".equals(o.getStatus())).count());
        counts.setProcessing((int) allOrders.stream().filter(o -> "processing".equals(o.getStatus())).count());
        counts.setShipped((int) allOrders.stream().filter(o -> "shipped".equals(o.getStatus())).count());
        counts.setCompleted((int) allOrders.stream().filter(o -> "completed".equals(o.getStatus())).count());
        counts.setCancelled((int) allOrders.stream().filter(o -> "cancelled".equals(o.getStatus())).count());

        return ResponseEntity.ok(ApiResponse.success(counts));
    }

    @Data
    public static class OrderStatusCounts {
        private int pending;
        private int processing;
        private int shipped;
        private int completed;
        private int cancelled;
    }
}