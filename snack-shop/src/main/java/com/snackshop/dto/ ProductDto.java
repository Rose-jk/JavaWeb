package com.snackshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private String fullDescription;
    private Double price;
    private Double originalPrice;
    private String category;
    private List<String> categories;
    private String imageUrl;
    private List<String> imageGallery;
    private Integer stock;
    private Double rating;
    private Integer reviewCount;
    private Integer sales;
    private List<String> tags;

    // 商品规格
    private String weight;
    private String size;
    private String flavor;
    private String brand;
    private String origin;
    private String expiryDate;
    private String storage;

    // 促销信息
    private Boolean onSale;
    private Double salePrice;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;

    // 推荐信息
    private Boolean recommended;
    private Boolean featured;
    private Boolean newArrival;

    // 状态
    private Boolean active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 计算字段（不在数据库中）
    private Double discount;
    private Double saveAmount;
    private Boolean inStock;
    private Boolean lowStock;
    private Boolean onSaleNow;
    private Double currentPrice;

    // 构造函数：从Entity转换
    public ProductDto(com.snackshop.model.Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.fullDescription = product.getFullDescription();
        this.price = product.getPrice();
        this.originalPrice = product.getOriginalPrice();
        this.category = product.getCategory();
        this.categories = product.getCategories();
        this.imageUrl = product.getImageUrl();
        this.imageGallery = product.getImageGallery();
        this.stock = product.getStock();
        this.rating = product.getRating();
        this.reviewCount = product.getReviewCount();
        this.sales = product.getSales();
        this.tags = product.getTags();

        // 商品规格
        this.weight = product.getWeight();
        this.size = product.getSize();
        this.flavor = product.getFlavor();
        this.brand = product.getBrand();
        this.origin = product.getOrigin();
        this.expiryDate = product.getExpiryDate();
        this.storage = product.getStorage();

        // 促销信息
        this.onSale = product.getOnSale();
        this.salePrice = product.getSalePrice();
        this.saleStartTime = product.getSaleStartTime();
        this.saleEndTime = product.getSaleEndTime();

        // 推荐信息
        this.recommended = product.getRecommended();
        this.featured = product.getFeatured();
        this.newArrival = product.getNewArrival();

        // 状态
        this.active = product.getActive();
        this.createTime = product.getCreateTime();
        this.updateTime = product.getUpdateTime();

        // 计算字段
        this.discount = product.getDiscount();
        this.saveAmount = product.getSaveAmount();
        this.inStock = product.isInStock();
        this.lowStock = product.isLowStock();
        this.onSaleNow = product.isOnSaleNow();
        this.currentPrice = product.getCurrentPrice();
    }

    // 转换为Entity
    public com.snackshop.model.Product toEntity() {
        com.snackshop.model.Product product = new com.snackshop.model.Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setFullDescription(this.fullDescription);
        product.setPrice(this.price);
        product.setOriginalPrice(this.originalPrice);
        product.setCategory(this.category);
        product.setCategories(this.categories);
        product.setImageUrl(this.imageUrl);
        product.setImageGallery(this.imageGallery);
        product.setStock(this.stock);
        product.setRating(this.rating);
        product.setReviewCount(this.reviewCount);
        product.setSales(this.sales);
        product.setTags(this.tags);

        // 商品规格
        product.setWeight(this.weight);
        product.setSize(this.size);
        product.setFlavor(this.flavor);
        product.setBrand(this.brand);
        product.setOrigin(this.origin);
        product.setExpiryDate(this.expiryDate);
        product.setStorage(this.storage);

        // 促销信息
        product.setOnSale(this.onSale);
        product.setSalePrice(this.salePrice);
        product.setSaleStartTime(this.saleStartTime);
        product.setSaleEndTime(this.saleEndTime);

        // 推荐信息
        product.setRecommended(this.recommended);
        product.setFeatured(this.featured);
        product.setNewArrival(this.newArrival);

        // 状态
        product.setActive(this.active);

        return product;
    }

    // 更新现有Entity
    public void updateEntity(com.snackshop.model.Product product) {
        if (this.name != null) product.setName(this.name);
        if (this.description != null) product.setDescription(this.description);
        if (this.fullDescription != null) product.setFullDescription(this.fullDescription);
        if (this.price != null) product.setPrice(this.price);
        if (this.originalPrice != null) product.setOriginalPrice(this.originalPrice);
        if (this.category != null) product.setCategory(this.category);
        if (this.categories != null) product.setCategories(this.categories);
        if (this.imageUrl != null) product.setImageUrl(this.imageUrl);
        if (this.imageGallery != null) product.setImageGallery(this.imageGallery);
        if (this.stock != null) product.setStock(this.stock);
        if (this.tags != null) product.setTags(this.tags);

        // 商品规格
        if (this.weight != null) product.setWeight(this.weight);
        if (this.size != null) product.setSize(this.size);
        if (this.flavor != null) product.setFlavor(this.flavor);
        if (this.brand != null) product.setBrand(this.brand);
        if (this.origin != null) product.setOrigin(this.origin);
        if (this.expiryDate != null) product.setExpiryDate(this.expiryDate);
        if (this.storage != null) product.setStorage(this.storage);

        // 促销信息
        if (this.onSale != null) product.setOnSale(this.onSale);
        if (this.salePrice != null) product.setSalePrice(this.salePrice);
        if (this.saleStartTime != null) product.setSaleStartTime(this.saleStartTime);
        if (this.saleEndTime != null) product.setSaleEndTime(this.saleEndTime);

        // 推荐信息
        if (this.recommended != null) product.setRecommended(this.recommended);
        if (this.featured != null) product.setFeatured(this.featured);
        if (this.newArrival != null) product.setNewArrival(this.newArrival);

        // 状态
        if (this.active != null) product.setActive(this.active);

        product.setUpdateTime(LocalDateTime.now());
    }

    // 创建简化的DTO（用于列表展示）
    public static ProductDto createSummary(com.snackshop.model.Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());
        dto.setStock(product.getStock());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setSales(product.getSales());
        dto.setTags(product.getTags());

        // 计算字段
        dto.setDiscount(product.getDiscount());
        dto.setSaveAmount(product.getSaveAmount());
        dto.setInStock(product.isInStock());
        dto.setOnSaleNow(product.isOnSaleNow());
        dto.setCurrentPrice(product.getCurrentPrice());

        return dto;
    }

    // 创建购物车DTO
    public static CartProductDto createCartProduct(com.snackshop.model.Product product, Integer quantity) {
        CartProductDto dto = new CartProductDto();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getCurrentPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setStock(product.getStock());
        dto.setQuantity(quantity);
        dto.setSubtotal(product.getCurrentPrice() * quantity);

        return dto;
    }

    // 内部类：购物车产品DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartProductDto {
        private String productId;
        private String name;
        private String imageUrl;
        private Double price;
        private Double originalPrice;
        private Integer stock;
        private Integer quantity;
        private Double subtotal;
        private Boolean inStock;

        public Boolean getInStock() {
            return stock >= quantity;
        }
    }
}