package com.example.myapplication;

import com.example.myapplication.Product;
import java.util.List;

public interface ProductService {
    void getById(int productId, ProductServiceCallback callback);  // Changed to int
    void getAll(ProductServiceCallback callback);

    interface ProductServiceCallback {
        void onProductFetched(Product product);
        void onProductsFetched(List<Product> products);
        void onFailure(String error);
    }
}
