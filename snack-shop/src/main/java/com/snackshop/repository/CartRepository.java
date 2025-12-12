package com.snackshop.repository;

import com.snackshop.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartItem, String> {
    List<CartItem> findByUserId(String userId);

    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

    void deleteByUserId(String userId);

    void deleteByUserIdAndProductId(String userId, String productId);

    @Query(value = "{'userId': ?0}", count = true)
    long countByUserId(String userId);
}