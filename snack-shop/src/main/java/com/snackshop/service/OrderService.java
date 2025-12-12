package com.snackshop.service;

import com.snackshop.model.*;
import com.snackshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;

    private static final DateTimeFormatter ORDER_NUMBER_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    @Transactional
    public Order createOrder(String userId, CreateOrderRequest request) {
        // 验证用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证地址
        Address address = addressRepository.findByIdAndUserId(request.getAddressId(), userId)
                .orElseThrow(() -> new RuntimeException("地址不存在"));

        // 获取购物车商品
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 创建订单项并验证库存
        List<Order.OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在: " + cartItem.getProductId()));

            // 验证库存
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }

            // 更新库存
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // 创建订单项
            Order.OrderItem orderItem = new Order.OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImageUrl());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItems.add(orderItem);
            subtotal += orderItem.getSubtotal();
        }

        // 计算运费和总金额
        double shippingFee = calculateShippingFee(subtotal);
        double discount = calculateDiscount(user, subtotal);
        double totalAmount = subtotal + shippingFee - discount;

        // 生成订单号
        String orderNumber = generateOrderNumber();

        // 创建订单
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserId(userId);

        // 设置收货信息
        order.setAddressId(address.getId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverAddress(formatAddress(address));

        // 设置订单商品
        order.setItems(orderItems);

        // 设置金额
        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setDiscount(discount);
        order.setTotalAmount(totalAmount);

        // 设置支付信息
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus("pending");

        // 设置订单状态
        order.setStatus("pending");
        order.setStatusNote("等待支付");

        order.setNote(request.getNote());

        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 清空购物车
        cartRepository.deleteByUserId(userId);

        // 更新用户累计消费
        user.setTotalSpent(user.getTotalSpent() + totalAmount);
        user.setPoints(user.getPoints() + (int) (totalAmount)); // 1元积1分
        userRepository.save(user);

        return savedOrder;
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public List<Order> getUserOrdersByStatus(String userId, String status) {
        return orderRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
    }

    public Order getOrderById(String orderId, String userId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Transactional
    public Order cancelOrder(String orderId, String userId) {
        Order order = getOrderById(orderId, userId);

        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("只有待付款订单可以取消");
        }

        // 恢复库存
        for (Order.OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus("cancelled");
        order.setStatusNote("用户取消订单");
        order.setUpdateTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Order payOrder(String orderId, String userId) {
        Order order = getOrderById(orderId, userId);

        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不支持支付");
        }

        order.setPaymentStatus("paid");
        order.setPaymentTime(LocalDateTime.now());
        order.setStatus("processing");
        order.setStatusNote("支付成功，等待发货");
        order.setUpdateTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Order shipOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!"processing".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不支持发货");
        }

        order.setStatus("shipped");
        order.setStatusNote("已发货");
        order.setUpdateTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(String orderId, String userId) {
        Order order = getOrderById(orderId, userId);

        if (!"shipped".equals(order.getStatus())) {
            throw new RuntimeException("只有已发货订单可以确认收货");
        }

        order.setStatus("completed");
        order.setStatusNote("交易完成");
        order.setUpdateTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(ORDER_NUMBER_FORMAT);
        String randomPart = String.format("%04d", (int) (Math.random() * 10000));
        return "SNACK" + datePart + randomPart;
    }

    private double calculateShippingFee(double subtotal) {
        // 满100免运费
        return subtotal >= 100 ? 0 : 10;
    }

    private double calculateDiscount(User user, double subtotal) {
        double discount = 0;

        // 新用户首次购物优惠
        if (user.getTotalSpent() == 0 && subtotal >= 50) {
            discount += 10;
        }

        // 会员等级优惠
        if (user.getPoints() >= 1000) {
            discount += subtotal * 0.05; // 5%折扣
        }

        return Math.min(discount, subtotal * 0.2); // 最高20%折扣
    }

    private String formatAddress(Address address) {
        return address.getProvince() + address.getCity() + address.getDistrict() + address.getDetail();
    }

    @Data
    public static class CreateOrderRequest {
        private String addressId;
        private String paymentMethod;
        private String note;
    }
}