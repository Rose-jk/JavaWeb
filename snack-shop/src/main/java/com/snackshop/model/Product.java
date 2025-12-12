package com.snackshop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @Indexed
    @TextIndexed
    private String name;

    private String description;

    @TextIndexed
    private String fullDescription;

    @Field("price")
    private Double price;

    @Field("original_price")
    private Double originalPrice;

    @Indexed
    private String category;

    private List<String> categories; // 多分类支持

    private String imageUrl;
    private List<String> imageGallery; // 多图展示

    @Field("stock")
    private Integer stock = 100;

    private Integer minStock = 10; // 库存预警值

    @Field("rating")
    private Double rating = 4.5;

    @Field("review_count")
    private Integer reviewCount = 0;

    @Field("sales")
    private Integer sales = 0;

    @Field("monthly_sales")
    private Integer monthlySales = 0;

    private List<String> tags;

    // 商品规格
    private String weight;
    private String size;
    private String flavor; // 口味
    private String brand; // 品牌
    private String origin; // 产地
    private String expiryDate; // 保质期
    private String storage; // 储存方式

    // 物流信息
    private Double weightKg; // 重量（公斤）
    private Double volume; // 体积（立方米）

    // 促销信息
    private Boolean onSale = false;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;
    private Double salePrice;

    // 推荐信息
    private Boolean recommended = false;
    private Boolean featured = false;
    private Boolean newArrival = true;

    // SEO信息
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;

    // 状态
    @Field("active")
    private Boolean active = true;

    // 时间戳
    @Field("create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Field("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    // 审核信息
    private String auditStatus = "approved"; // pending, approved, rejected
    private String auditNote;
    private String auditor;
    private LocalDateTime auditTime;

    // 供应商信息
    private String supplierId;
    private String supplierName;
    private Double costPrice; // 成本价

    // 获取折扣信息
    public Double getDiscount() {
        if (originalPrice != null && originalPrice > price) {
            return ((originalPrice - price) / originalPrice) * 100;
        }
        return 0.0;
    }

    // 获取节省金额
    public Double getSaveAmount() {
        if (originalPrice != null && originalPrice > price) {
            return originalPrice - price;
        }
        return 0.0;
    }

    // 是否在促销中
    public Boolean isOnSaleNow() {
        if (!onSale || saleStartTime == null || saleEndTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(saleStartTime) && now.isBefore(saleEndTime);
    }

    // 获取当前价格（考虑促销）
    public Double getCurrentPrice() {
        if (isOnSaleNow() && salePrice != null && salePrice > 0) {
            return salePrice;
        }
        return price;
    }

    // 检查库存
    public Boolean isInStock() {
        return stock > 0;
    }

    public Boolean isLowStock() {
        return stock <= minStock;
    }

    // 减少库存
    public Boolean decreaseStock(Integer quantity) {
        if (quantity <= 0 || stock < quantity) {
            return false;
        }
        stock -= quantity;
        sales += quantity;
        monthlySales += quantity;
        updateTime = LocalDateTime.now();
        return true;
    }

    // 增加库存
    public void increaseStock(Integer quantity) {
        if (quantity > 0) {
            stock += quantity;
            updateTime = LocalDateTime.now();
        }
    }

    // 更新评分
    public void updateRating(Double newRating) {
        if (newRating >= 0 && newRating <= 5) {
            double totalScore = rating * reviewCount + newRating;
            reviewCount++;
            rating = totalScore / reviewCount;
            updateTime = LocalDateTime.now();
        }
    }
}