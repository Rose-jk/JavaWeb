package com.snackshop.config;

import com.snackshop.model.*;
import com.snackshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        return args -> {
            // 清空数据库（仅在开发环境使用）
            // userRepository.deleteAll();
            // productRepository.deleteAll();
            // categoryRepository.deleteAll();

            // 初始化管理员用户
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@snackshop.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPhone("13800138000");
                admin.setRole("ROLE_ADMIN");
                admin.setPoints(1000);
                admin.setTotalSpent(5000.0);
                userRepository.save(admin);

                User user = new User();
                user.setUsername("testuser");
                user.setEmail("user@snackshop.com");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setPhone("13900139000");
                user.setBirthdate(LocalDate.of(2000, 1, 1));
                user.setPoints(500);
                user.setTotalSpent(1000.0);
                userRepository.save(user);

                System.out.println("初始化用户数据完成");
            }

            // 初始化商品分类
            if (categoryRepository.count() == 0) {
                List<Category> categories = Arrays.asList(
                        createCategory("薯片膨化", "各种薯片、虾条等膨化食品", "fas fa-cookie", "#FF9F43"),
                        createCategory("饼干糕点", "饼干、蛋糕、面包等", "fas fa-birthday-cake", "#FF6B6B"),
                        createCategory("糖果巧克力", "各类糖果、巧克力", "fas fa-candy-cane", "#EE5A24"),
                        createCategory("坚果炒货", "坚果、瓜子、花生等", "fas fa-seedling", "#A3CB38"),
                        createCategory("肉干肉脯", "牛肉干、猪肉脯等", "fas fa-drumstick-bite", "#ED4C67"),
                        createCategory("进口零食", "进口食品、特色零食", "fas fa-globe-americas", "#12CBC4"),
                        createCategory("饮料冲调", "饮品、冲调类食品", "fas fa-coffee", "#FDA7DF"),
                        createCategory("方便速食", "方便面、自热食品等", "fas fa-utensils", "#D980FA")
                );

                categoryRepository.saveAll(categories);
                System.out.println("初始化分类数据完成");
            }

            // 初始化商品数据
            if (productRepository.count() == 0) {
                List<Product> products = Arrays.asList(
                        createProduct("香脆薯片组合", "三种口味混合装", 25.99, 32.99, "薯片膨化",
                                "https://images.unsplash.com/photo-1566478989037-eec170784d0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"畅销", "组合装", "新品"}),

                        createProduct("巧克力夹心饼干", "浓郁巧克力夹心", 18.50, null, "饼干糕点",
                                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"巧克力", "饼干", "进口"}),

                        createProduct("混合坚果礼盒", "五种坚果混合", 45.99, 55.99, "坚果炒货",
                                "https://images.unsplash.com/photo-1532939624-3af1308db9a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"健康", "礼盒", "坚果"}),

                        createProduct("果味软糖大礼包", "多种水果口味", 22.80, null, "糖果巧克力",
                                "https://images.unsplash.com/photo-1563805042-7684c019e1cb?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"软糖", "水果味", "大礼包"}),

                        createProduct("香辣牛肉干", "传统工艺制作", 38.50, null, "肉干肉脯",
                                "https://images.unsplash.com/photo-1546833999-b9f581a1996d?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"牛肉干", "香辣", "传统"}),

                        createProduct("日本进口抹茶饼干", "正宗宇治抹茶", 32.99, null, "进口零食",
                                "https://images.unsplash.com/photo-1558322456-bb57647bf5f0?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"日本", "抹茶", "进口"}),

                        createProduct("海苔脆片", "低卡健康零食", 15.99, null, "薯片膨化",
                                "https://images.unsplash.com/photo-1516100882582-96c3a05fe590?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"海苔", "健康", "低卡"}),

                        createProduct("比利时黑巧克力", "72%可可含量", 65.00, 78.00, "糖果巧克力",
                                "https://images.unsplash.com/photo-1511381939415-e44015466834?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
                                new String[]{"比利时", "黑巧克力", "进口"})
                );

                productRepository.saveAll(products);
                System.out.println("初始化商品数据完成");
            }
        };
    }

    private Category createCategory(String name, String description, String icon, String color) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIcon(icon);
        category.setColor(color);
        category.setProductCount(10);
        category.setSortOrder(1);
        category.setActive(true);
        return category;
    }

    private Product createProduct(String name, String description, Double price,
                                  Double originalPrice, String category, String imageUrl, String[] tags) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setFullDescription(description + "，美味可口，休闲必备。独立包装，方便携带。");
        product.setPrice(price);
        product.setOriginalPrice(originalPrice);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setTags(tags);
        product.setWeight("150g");
        product.setExpiryDate("12个月");
        product.setOrigin("中国");
        product.setStorage("阴凉干燥处");
        product.setStock(100);
        product.setSales((int)(Math.random() * 100));
        product.setRating(4.0 + Math.random() * 1.0);
        product.setReviewCount((int)(Math.random() * 200));
        return product;
    }
}