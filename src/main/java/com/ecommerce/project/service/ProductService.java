package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(ProductDTO product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchProductsByCategory(Long categoryId);

    ProductResponse searchProductsBykeyword(String keyword);

    ProductDTO updateProductById(long productId, ProductDTO product);

    ProductDTO deleteProductById(long productId);
}
