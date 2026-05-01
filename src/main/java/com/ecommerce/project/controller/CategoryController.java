package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category Added Succefully", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try {
            String status = categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(status);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category){
        try{
            Category category1 = categoryService.updateCategorybyId(categoryId, category);
            return new ResponseEntity<>("Category with categorId " + categoryId + " is updated", HttpStatus.CREATED);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());

        }
    }
}
