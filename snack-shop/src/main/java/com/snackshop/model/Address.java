package com.snackshop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "addresses")
public class Address {
    @Id
    private String id;

    @Indexed
    @Field("user_id")
    private String userId;

    // 联系人信息
    private String name;
    private String phone;

    // 地址信息
    private String province;
    private String city;
    private String district;
    private String street;
    private String detail;

    @Field("postal_code")
    private String postalCode;

    // 地址标签（家、公司、学校等）
    private String tag;

    // 是否为默认地址
    @Field("is_default")
    private Boolean isDefault = false;

    // 地址验证状态
    private String status = "valid"; // valid, invalid, pending
    private String validationNote;

    // 物流信息
    private String shippingArea; // 配送区域
    private Double shippingFee; // 该地址的运费
    private Integer deliveryTime; // 预计配送时间（小时）

    // 时间戳
    @Field("create_time")
    private LocalDateTime createTime = LocalDateTime.now();

    @Field("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    // 使用次数统计
    @Field("use_count")
    private Integer useCount = 0;

    @Field("last_used")
    private LocalDateTime lastUsed;

    // 是否有效（软删除）
    @Field("active")
    private Boolean active = true;

    // 获取完整地址
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) sb.append(province);
        if (city != null) sb.append(city);
        if (district != null) sb.append(district);
        if (street != null) sb.append(street);
        if (detail != null) sb.append(detail);
        return sb.toString();
    }

    // 获取格式化地址（带分隔符）
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) {
            sb.append(province);
            if (city != null && !city.equals(province)) {
                sb.append(" ").append(city);
            }
        }
        if (district != null) sb.append(" ").append(district);
        if (street != null) sb.append(" ").append(street);
        if (detail != null) sb.append(" ").append(detail);
        return sb.toString().trim();
    }

    // 地址简写（用于显示）
    public String getShortAddress() {
        if (city != null && district != null) {
            return city + " " + district;
        }
        return getFormattedAddress();
    }

    // 更新使用时间
    public void markAsUsed() {
        this.useCount++;
        this.lastUsed = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 验证地址格式
    public Boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                phone != null && phone.matches("^1[3-9]\\d{9}$") &&
                province != null && !province.trim().isEmpty() &&
                city != null && !city.trim().isEmpty() &&
                detail != null && !detail.trim().isEmpty();
    }

    // 检查是否为同一地址
    public Boolean isSameAddress(Address other) {
        if (other == null) return false;
        return province != null && province.equals(other.getProvince()) &&
                city != null && city.equals(other.getCity()) &&
                district != null && district.equals(other.getDistrict()) &&
                detail != null && detail.equals(other.getDetail());
    }
}