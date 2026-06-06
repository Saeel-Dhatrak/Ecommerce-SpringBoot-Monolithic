package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchProductsByCategory(Long categoryId);

    ProductResponse searchProductsBykeyword(String keyword);

    ProductDTO updateProductById(long productId, Product product);

    ProductDTO deleteProductById(long productId);
}
