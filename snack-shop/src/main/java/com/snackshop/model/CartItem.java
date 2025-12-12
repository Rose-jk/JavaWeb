package com.snackshop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "cart_items")
public class CartItem {
    @Id
    private String id;

    private String userId;
    private String productId;
    private Integer quantity;
    private LocalDateTime addedAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 引用产品信息（非规范化设计，便于查询）
    private String productName;
    private String productImage;
    private Double productPrice;

    // 计算小计
    public Double getSubtotal() {
        return productPrice * quantity;
    }
}