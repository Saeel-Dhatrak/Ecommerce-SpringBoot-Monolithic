- Started with Creating the Category Module
- ```java
    package com.ecommerce.project.model;
    
    public class Category {
    
        private Long categoryId;
        private String categoryName;
    
        public Category(Long categoryId) {
            this.categoryId = categoryId;
        }
    
        public Long getCategoryId() {
            return categoryId;
        }
    
        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    
        public String getCategoryName() {
            return categoryName;
        }
    
        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    
        @Override
        public String toString() {
            return "Category{" +
                    "categoryId=" + categoryId +
                    ", categoryName='" + categoryName + '\'' +
                    '}';
        }
    }
    // ------------------------------------------------------------------------------------------------
    package com.ecommerce.project.service;
    
    import com.ecommerce.project.model.Category;
    
    import java.util.List;
    
    public interface CategoryService {
    public List<Category> getAllCategories();
    void createCategory(Category category);
    
        String deleteCategory(Long categoryId);
    }
    // ------------------------------------------------------------------------------------------------
    package com.ecommerce.project.service;
    
    import com.ecommerce.project.model.Category;
    import org.springframework.stereotype.Service;
    
    import java.util.ArrayList;
    import java.util.List;
    
    @Service
    public class CategoryServiceImpl implements CategoryService{
    
        private final List<Category> categories = new ArrayList<>();
        private Long nextId = 1L;
    
        @Override
        public List<Category> getAllCategories() {
            return categories;
        }
    
        @Override
        public void createCategory(Category category) {
            category.setCategoryId(nextId++);
            categories.add(category);
        }
    
        @Override
        public String deleteCategory(Long categoryId) {
            Category category = categories.stream()
                    .filter(c -> c.getCategoryId().equals(categoryId))
                    .findFirst().orElse(null);
            if(category == null){
                return "Category not found";
            }
            categories.remove(category);
            return "Category with id : " + categoryId +  " Deleted successfully";
        }
    }
    
    // ------------------------------------------------------------------------------------------------
    package com.ecommerce.project.controller;
    
    import com.ecommerce.project.model.Category;
    import com.ecommerce.project.service.CategoryService;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.ArrayList;
    import java.util.List;
    
    @RestController
    public class CategoryController {
    
        private final CategoryService categoryService;
    
        public CategoryController(CategoryService categoryService) {
            this.categoryService = categoryService;
        }
    
        @GetMapping("/api/public/categories")
        public List<Category> getAllCategories(){
            return categoryService.getAllCategories();
        }
    
        @PostMapping("/api/public/categories")
        public String createCategory(@RequestBody Category category){
            categoryService.createCategory(category);
            return "Category Created Successfully";
        }
    
        @DeleteMapping("/api/admin/category/{categoryId}")
        public String deleteCategory(@PathVariable Long categoryId){
           String status = categoryService.deleteCategory(categoryId);
           return status;
        }
    }
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------
  ```
- @PathVariable annotation takes the value mentioned in the curly braces of the url and passes it on to the function.
- Category category = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst().orElse(null);
- What we have done above is we have cinverted the list of categories into stream then we have applied the filter on it and use the functional programming using which we will check whether the id that we are sending to delete with every other categoryID that exists
- for remove method to work on the list we need to get the category object and hence using the stream we got the exact category object against that particukar categoryID
- **ResponseEntity**
- Get the better response codes
- ```java
    @DeleteMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
    // Service Impl
    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().orElseThrow( ()  -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
        if(category == null){
            return "Category not found";
        }
        categories.remove(category);
        return "Category with id : " + categoryId +  " Deleted successfully";
    }
  ```