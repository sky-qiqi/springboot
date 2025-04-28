package com.example.t5springboot.service;

import com.example.t5springboot.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Integer id);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Integer id);
}