package com.snackshop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String phone;
    private LocalDate birthdate;
    private String avatar;
    private String role = "ROLE_USER";
    private LocalDateTime registerDate = LocalDateTime.now();
    private LocalDateTime lastLogin;
    private Integer points = 0;
    private Double totalSpent = 0.0;
    private Boolean enabled = true;

    // 用户等级信息
    private String memberLevel = "普通会员";
    private Integer orderCount = 0;

    // 微信/支付宝绑定信息（可选）
    private String wechatOpenId;
    private String alipayUserId;

    // 用户偏好设置
    private Boolean emailNotifications = true;
    private Boolean smsNotifications = false;
    private String language = "zh-CN";
    private String timezone = "Asia/Shanghai";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email; // Spring Security使用邮箱作为用户名
    }

    public String getDisplayName() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }

    // 更新最后登录时间
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    // 添加积分
    public void addPoints(Integer points) {
        if (points != null && points > 0) {
            this.points += points;
            updateMemberLevel();
        }
    }

    // 消费后更新
    public void afterPurchase(Double amount) {
        if (amount != null && amount > 0) {
            this.totalSpent += amount;
            // 每消费1元积1分
            int earnedPoints = amount.intValue();
            addPoints(earnedPoints);
            this.orderCount++;
        }
    }

    // 更新会员等级
    private void updateMemberLevel() {
        if (totalSpent >= 10000) {
            memberLevel = "钻石会员";
        } else if (totalSpent >= 5000) {
            memberLevel = "黄金会员";
        } else if (totalSpent >= 1000) {
            memberLevel = "白银会员";
        } else {
            memberLevel = "普通会员";
        }
    }

    // 获取用户折扣率
    public Double getDiscountRate() {
        switch (memberLevel) {
            case "钻石会员":
                return 0.9; // 9折
            case "黄金会员":
                return 0.92; // 9.2折
            case "白银会员":
                return 0.95; // 9.5折
            default:
                return 1.0; // 无折扣
        }
    }
}