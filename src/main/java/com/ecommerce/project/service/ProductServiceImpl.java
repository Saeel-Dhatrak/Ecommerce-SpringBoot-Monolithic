package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Category", "categoryID", categoryId));
        Product product = modelMapper.map(productDto, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) * product.getPrice());
        // 100 - ((25 * 0.01) * 100)
        // 100 - (0.25) * 100
        // 100 - 25 = 75 rupees is the special price after the discount of 25
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDto = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDto);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
                "Category", "categoryId", categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsBykeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProductById(long productId, ProductDTO productDto) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        Product product = modelMapper.map(productDto, Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct = productRepository.save(existingProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProductById(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("productId", "Product", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }
}
