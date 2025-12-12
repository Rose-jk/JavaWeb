package com.snackshop.model;

public enum OrderStatus {
    PENDING("待付款"),
    PROCESSING("处理中"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}