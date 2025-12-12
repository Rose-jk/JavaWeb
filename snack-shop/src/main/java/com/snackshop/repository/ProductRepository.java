package com.snackshop.repository;

import com.snackshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // 基本查询
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByActiveTrue();
    List<Product> findByActiveFalse();

    // 分页查询
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);

    // 条件查询
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findByStockGreaterThan(Integer stock);
    List<Product> findByStockLessThanEqual(Integer stock);
    List<Product> findByRatingGreaterThanEqual(Double minRating);

    // 促销商品
    List<Product> findByOnSaleTrue();
    List<Product> findByOnSaleTrueAndActiveTrue();

    // 推荐商品
    List<Product> findByRecommendedTrueAndActiveTrue();
    List<Product> findByFeaturedTrueAndActiveTrue();
    List<Product> findByNewArrivalTrueAndActiveTrue();

    // 多条件组合查询
    List<Product> findByCategoryAndPriceBetween(String category, Double minPrice, Double maxPrice);
    List<Product> findByCategoryAndRatingGreaterThanEqual(String category, Double minRating);

    // 排序查询
    List<Product> findByActiveTrueOrderByPriceAsc();
    List<Product> findByActiveTrueOrderByPriceDesc();
    List<Product> findByActiveTrueOrderBySalesDesc();
    List<Product> findByActiveTrueOrderByRatingDesc();
    List<Product> findByActiveTrueOrderByCreateTimeDesc();

    // 文本搜索
    @Query("{ '$text': { '$search': ?0 }, 'active': true }")
    List<Product> fullTextSearch(String keyword);

    @Query("{ '$or': [ "
            + "{ 'name': { '$regex': ?0, '$options': 'i' } }, "
            + "{ 'description': { '$regex': ?0, '$options': 'i' } }, "
            + "{ 'fullDescription': { '$regex': ?0, '$options': 'i' } }, "
            + "{ 'tags': { '$regex': ?0, '$options': 'i' } } "
            + "], 'active': true }")
    List<Product> searchProducts(String keyword);

    // 复杂查询
    @Query("{ 'category': ?0, 'price': { '$gte': ?1, '$lte': ?2 }, 'stock': { '$gt': 0 }, 'active': true }")
    List<Product> findAvailableProductsByCategoryAndPriceRange(String category, Double minPrice, Double maxPrice);

    // 标签查询
    @Query("{ 'tags': ?0, 'active': true }")
    List<Product> findByTag(String tag);

    @Query("{ 'tags': { '$in': ?0 }, 'active': true }")
    List<Product> findByTagsIn(List<String> tags);

    // 供应商查询
    List<Product> findBySupplierId(String supplierId);
    List<Product> findBySupplierIdAndActiveTrue(String supplierId);

    // 批量查询
    List<Product> findByIdIn(List<String> ids);
    List<Product> findByIdInAndActiveTrue(List<String> ids);

    // 统计查询
    Long countByCategory(String category);
    Long countByActiveTrue();
    Long countByStockLessThanEqual(Integer stock);

    // 聚合查询示例
    @Query(value = "{ 'category': ?0 }", count = true)
    Long countProductsByCategory(String category);

    @Query(value = "{ 'stock': { '$lte': ?0 } }", count = true)
    Long countLowStockProducts(Integer threshold);

    // 更新操作
    @Query("{ '_id': ?0 }")
    void decreaseStockById(String id, Integer quantity);

    @Query("{ '_id': ?0 }")
    void increaseStockById(String id, Integer quantity);

    @Query("{ '_id': ?0 }")
    void updateSalesById(String id, Integer quantity);

    // 价格范围搜索
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'active': true }")
    List<Product> findByPriceRange(Double minPrice, Double maxPrice);

    // 多分类查询
    @Query("{ 'categories': { '$in': ?0 }, 'active': true }")
    List<Product> findByCategoriesIn(List<String> categories);

    // 获取热门商品
    @Query(value = "{ 'active': true }", sort = "{ 'sales': -1 }")
    List<Product> findTopSellingProducts(Pageable pageable);

    @Query(value = "{ 'active': true }", sort = "{ 'monthlySales': -1 }")
    List<Product> findTopMonthlyProducts(Pageable pageable);

    // 获取新上架商品
    @Query("{ 'createTime': { '$gte': ?0 }, 'active': true }")
    List<Product> findNewProductsSince(LocalDateTime since);

    // 促销商品查询
    @Query("{ 'onSale': true, 'saleStartTime': { '$lte': ?0 }, 'saleEndTime': { '$gte': ?0 }, 'active': true }")
    List<Product> findActiveSaleProducts(LocalDateTime now);

    // 根据品牌查询
    List<Product> findByBrandAndActiveTrue(String brand);

    // 根据产地查询
    List<Product> findByOriginAndActiveTrue(String origin);

    // 自定义返回字段
    @Query(value = "{ '_id': ?0 }", fields = "{ 'id': 1, 'name': 1, 'price': 1, 'imageUrl': 1, 'stock': 1 }")
    Optional<Product> findBasicInfoById(String id);

    // 检查库存
    @Query(value = "{ '_id': ?0, 'stock': { '$gte': ?1 } }", exists = true)
    Boolean checkStock(String productId, Integer quantity);

    // 批量更新
    @Query("{ '_id': { '$in': ?0 } }")
    List<Product> updateMultipleProducts(List<String> ids, Product update);
}