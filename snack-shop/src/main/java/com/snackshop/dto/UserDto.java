package com.snackshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String id;
    private String userId;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String street;
    private String detail;
    private String postalCode;
    private String tag;
    private Boolean isDefault;
    private String fullAddress;
    private String shortAddress;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 构造函数：从Entity转换
    public AddressDto(com.snackshop.model.Address address) {
        this.id = address.getId();
        this.userId = address.getUserId();
        this.name = address.getName();
        this.phone = address.getPhone();
        this.province = address.getProvince();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.street = address.getStreet();
        this.detail = address.getDetail();
        this.postalCode = address.getPostalCode();
        this.tag = address.getTag();
        this.isDefault = address.getIsDefault();
        this.fullAddress = address.getFullAddress();
        this.shortAddress = address.getShortAddress();
        this.createTime = address.getCreateTime();
        this.updateTime = address.getUpdateTime();
    }

    // 转换为Entity
    public com.snackshop.model.Address toEntity() {
        com.snackshop.model.Address address = new com.snackshop.model.Address();
        address.setName(this.name);
        address.setPhone(this.phone);
        address.setProvince(this.province);
        address.setCity(this.city);
        address.setDistrict(this.district);
        address.setStreet(this.street);
        address.setDetail(this.detail);
        address.setPostalCode(this.postalCode);
        address.setTag(this.tag);
        address.setIsDefault(this.isDefault);

        return address;
    }

    // 更新现有Entity
    public void updateEntity(com.snackshop.model.Address address) {
        if (this.name != null) address.setName(this.name);
        if (this.phone != null) address.setPhone(this.phone);
        if (this.province != null) address.setProvince(this.province);
        if (this.city != null) address.setCity(this.city);
        if (this.district != null) address.setDistrict(this.district);
        if (this.street != null) address.setStreet(this.street);
        if (this.detail != null) address.setDetail(this.detail);
        if (this.postalCode != null) address.setPostalCode(this.postalCode);
        if (this.tag != null) address.setTag(this.tag);
        if (this.isDefault != null) address.setIsDefault(this.isDefault);
    }

    // 验证地址
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                phone != null && phone.matches("^1[3-9]\\d{9}$") &&
                province != null && !province.trim().isEmpty() &&
                city != null && !city.trim().isEmpty() &&
                detail != null && !detail.trim().isEmpty();
    }
}