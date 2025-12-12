package com.snackshop.repository;

import com.snackshop.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByActiveTrueOrderBySortOrderAsc();

    List<Category> findByNameContainingIgnoreCase(String name);

    @Query("{'active': true}")
    List<Category> findAllActiveCategories();
}