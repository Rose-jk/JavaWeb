package com.snackshop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private String orderNumber; // 订单号，格式：SNACK+年月日+序号
    private String userId;

    // 收货信息
    private String addressId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    // 订单商品
    private List<OrderItem> items;

    // 订单金额
    private Double subtotal; // 商品总价
    private Double shippingFee; // 运费
    private Double discount; // 优惠金额
    private Double totalAmount; // 实付金额

    // 支付信息
    private String paymentMethod; // 支付方式：alipay, wechat, card
    private String paymentStatus; // 支付状态：pending, paid, refunded
    private LocalDateTime paymentTime;

    // 订单状态
    private String status; // pending, processing, shipped, completed, cancelled
    private String statusNote; // 状态说明

    // 时间戳
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    // 订单备注
    private String note;

    // 是否评价
    private Boolean reviewed = false;

    @Data
    public static class OrderItem {
        private String productId;
        private String productName;
        private String productImage;
        private Double price;
        private Integer quantity;

        public Double getSubtotal() {
            return price * quantity;
        }
    }
}