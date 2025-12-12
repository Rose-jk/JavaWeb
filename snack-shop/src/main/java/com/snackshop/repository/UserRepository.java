package com.snackshop.repository;

import com.snackshop.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // 基本查询
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);

    // 存在性检查
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByPhone(String phone);

    // 多条件查询
    Optional<User> findByEmailAndEnabledTrue(String email);
    List<User> findByRole(String role);
    List<User> findByMemberLevel(String memberLevel);

    // 统计查询
    Long countByRole(String role);
    Long countByMemberLevel(String memberLevel);
    Long countByRegisterDateBetween(LocalDateTime start, LocalDateTime end);

    // 范围查询
    List<User> findByPointsGreaterThanEqual(Integer points);
    List<User> findByTotalSpentGreaterThanEqual(Double amount);
    List<User> findByRegisterDateAfter(LocalDateTime date);

    // 复杂查询
    @Query("{ '$or': [ { 'email': ?0 }, { 'username': ?0 }, { 'phone': ?0 } ] }")
    Optional<User> findByEmailOrUsernameOrPhone(String identifier);

    @Query("{ 'enabled': true, '$or': [ { 'email': ?0 }, { 'username': ?0 } ] }")
    Optional<User> findActiveUserByIdentifier(String identifier);

    // 分页和排序查询
    @Query("{ 'role': ?0 }")
    List<User> findUsersByRoleWithSort(String role, org.springframework.data.domain.Sort sort);

    // 更新操作
    @Query("{ '_id': ?0 }")
    void updateLastLoginById(String id, LocalDateTime lastLogin);

    @Query("{ '_id': ?0 }")
    void addPointsById(String id, Integer points);

    @Query("{ '_id': ?0 }")
    void updateTotalSpentById(String id, Double amount);

    // 搜索用户
    @Query("{ '$or': [ "
            + "{ 'username': { '$regex': ?0, '$options': 'i' } }, "
            + "{ 'email': { '$regex': ?0, '$options': 'i' } }, "
            + "{ 'phone': { '$regex': ?0, '$options': 'i' } } "
            + "] }")
    List<User> searchUsers(String keyword);

    // 根据注册日期范围查询
    List<User> findByRegisterDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 根据最后登录时间查询活跃用户
    List<User> findByLastLoginAfter(LocalDateTime date);

    // 批量查询
    List<User> findByIdIn(List<String> ids);

    // 自定义投影（只返回需要的字段）
    @Query(value = "{ 'email': ?0 }", fields = "{ 'id': 1, 'username': 1, 'email': 1, 'avatar': 1 }")
    Optional<User> findBasicInfoByEmail(String email);

    // 统计用户消费
    @Query(value = "{}", fields = "{ 'totalSpent': 1 }")
    List<User> findAllWithTotalSpent();

    // 获取用户统计信息
    @Query(value = "{}", count = true)
    Long countAllUsers();

    @Query(value = "{ 'enabled': true }", count = true)
    Long countActiveUsers();

    @Query(value = "{ 'enabled': false }", count = true)
    Long countInactiveUsers();

    // 聚合查询示例：按会员等级分组统计
    @Query(value = "{}", sort = "{ 'totalSpent': -1 }")
    List<User> findTopSpenders(int limit);

    // 删除标记的用户
    List<User> findByEnabledFalse();

    // 清理不活跃用户（示例）
    @Query("{ 'lastLogin': { '$lt': ?0 }, 'enabled': true }")
    List<User> findInactiveUsers(LocalDateTime cutoffDate);
}