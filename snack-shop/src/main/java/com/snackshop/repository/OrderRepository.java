package com.snackshop.repository;

import com.snackshop.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserIdOrderByCreateTimeDesc(String userId);

    List<Order> findByUserIdAndStatusOrderByCreateTimeDesc(String userId, String status);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByStatus(String status);

    @Query("{'createTime': {$gte: ?0, $lte: ?1}}")
    List<Order> findByCreateTimeBetween(String start, String end);

    long countByUserId(String userId);

    @Query(value = "{'userId': ?0}", count = true)
    long countOrdersByUserId(String userId);
}