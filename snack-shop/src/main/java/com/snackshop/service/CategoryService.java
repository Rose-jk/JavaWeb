package com.snackshop.service;

import com.snackshop.model.Category;
import com.snackshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public List<Category> getAllCategories() {
        return categoryRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
    }

    public Category createCategory(Category category) {
        // 检查分类名是否已存在
        if (categoryRepository.findByNameContainingIgnoreCase(category.getName()).stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(category.getName()))) {
            throw new RuntimeException("分类名已存在");
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(String id, Category category) {
        Category existingCategory = getCategoryById(id);

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setIcon(category.getIcon());
        existingCategory.setColor(category.getColor());
        existingCategory.setSortOrder(category.getSortOrder());
        existingCategory.setActive(category.getActive());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(String id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }

    public void updateProductCount(String categoryName) {
        // 在实际项目中，这里应该更新分类的商品数量
        // 为简化，这里只是占位符
    }
}