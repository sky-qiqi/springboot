package com.example.t5springboot.service.impl;

import com.example.t5springboot.mapper.ProductMapper;
import com.example.t5springboot.model.Product;
import com.example.t5springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Cacheable(value = "products", key = "'all'") // Cache all products result
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    @Override
    @Cacheable(value = "product", key = "#id") // Cache product by id
    public Product getProductById(Integer id) {
        return productMapper.findById(id);
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true) // Evict all product caches on add
    public Product addProduct(Product product) {
        productMapper.insert(product);
        return product; // Return the product with generated ID
    }

    @Override
    @CachePut(value = "product", key = "#product.id") // Update cache for the specific product
    @CacheEvict(value = "products", key = "'all'") // Evict the 'all products' cache
    public Product updateProduct(Product product) {
        productMapper.update(product);
        return productMapper.findById(product.getId()); // Return updated product from DB to update cache
    }

    @Override
    @CacheEvict(value = {"products", "product"}, allEntries = true) // Evict all product caches on delete
    public void deleteProduct(Integer id) {
        productMapper.deleteById(id);
    }
}