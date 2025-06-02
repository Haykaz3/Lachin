package com.example.myapplication;

import com.example.myapplication.Product;

import java.io.File;
import java.util.List;

public interface ProductService {
    void getById(int productId, ProductServiceCallback callback);  // Changed to int
    void getAll(ProductServiceCallback callback);
    void addProduct(ProductDTO product, List<File> images, ProductAddCallback callback);

    interface ProductAddCallback {
        void onSuccess();
        void onFailure(String error);
    }

    interface ProductServiceCallback {
        void onProductFetched(Product product);
        void onProductsFetched(List<Product> products);
        void onFailure(String error);
    }

}
