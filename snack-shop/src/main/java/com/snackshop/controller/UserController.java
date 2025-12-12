package com.snackshop.controller;

import com.snackshop.dto.ApiResponse;
import com.snackshop.model.Address;
import com.snackshop.model.User;
import com.snackshop.service.AddressService;
import com.snackshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile(@AuthenticationPrincipal User user) {
        User userDetails = userService.getUserById(user.getId());
        return ResponseEntity.ok(ApiResponse.success(userDetails));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<User>> updateUserProfile(
            @AuthenticationPrincipal User currentUser,
            @RequestBody User updatedUser) {
        User user = userService.getUserById(currentUser.getId());

        // 只允许更新部分字段
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getAvatar() != null) {
            user.setAvatar(updatedUser.getAvatar());
        }

        User savedUser = userService.updateUser(user);
        return ResponseEntity.ok(ApiResponse.success("个人信息更新成功", savedUser));
    }

    // 地址管理
    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<Address>>> getUserAddresses(@AuthenticationPrincipal User user) {
        List<Address> addresses = addressService.getUserAddresses(user.getId());
        return ResponseEntity.ok(ApiResponse.success(addresses));
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<Address>> getAddressById(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        Address address = addressService.getAddressById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<Address>> addAddress(
            @AuthenticationPrincipal User user,
            @RequestBody Address address) {
        Address savedAddress = addressService.addAddress(user.getId(), address);
        return ResponseEntity.ok(ApiResponse.success("地址添加成功", savedAddress));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(
            @PathVariable String id,
            @AuthenticationPrincipal User user,
            @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(id, user.getId(), address);
        return ResponseEntity.ok(ApiResponse.success("地址更新成功", updatedAddress));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        addressService.deleteAddress(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("地址删除成功", null));
    }

    @PutMapping("/addresses/{id}/default")
    public ResponseEntity<ApiResponse<Address>> setDefaultAddress(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        Address address = addressService.setDefaultAddress(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("默认地址设置成功", address));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<UserStats>> getUserStats(@AuthenticationPrincipal User user) {
        UserStats stats = new UserStats();
        stats.setTotalOrders(10); // 这里应该从数据库查询
        stats.setTotalSpent(user.getTotalSpent());
        stats.setPoints(user.getPoints());
        stats.setMemberLevel(calculateMemberLevel(user.getPoints()));

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    private String calculateMemberLevel(int points) {
        if (points >= 5000) return "钻石会员";
        if (points >= 2000) return "黄金会员";
        if (points >= 1000) return "白银会员";
        return "普通会员";
    }

    @Data
    private static class UserStats {
        private long totalOrders;
        private double totalSpent;
        private int points;
        private String memberLevel;
    }

    // 辅助方法 - 实际项目中应该移动到UserService
    private User updateUser(User user) {
        return userService.getUserById(user.getId()); // 简化实现
    }
}