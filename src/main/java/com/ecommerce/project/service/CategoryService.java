package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategories();
    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    //Category findbyCategoryId(Long categoryId);

    Category updateCategorybyId(Long categoryId, Category category);
}
