package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.Product;
import com.example.myapplication.ProductService;
import com.example.myapplication.ProductServiceImpl;


import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private TextView productName, productDescription, productPrice, categoryName;
    private LinearLayout attributesContainer;
    private ProductService productService;
    private ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        categoryName = findViewById(R.id.categoryName);
        attributesContainer = findViewById(R.id.attributesContainer);
        productImage = findViewById(R.id.productImage);

        productService = new ProductServiceImpl();

        // Fetching product by ID
        int productId = 12;  // This is an int now
        fetchProductById(productId);
    }

    private void fetchProductById(int productId) {
        productService.getById(productId, new ProductService.ProductServiceCallback() {
            @Override
            public void onProductFetched(Product product) {
                showProduct(product);
            }

            @Override
            public void onProductsFetched(List<Product> products) {
                // Handle case if you were fetching a list of products (not used here)
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ProductActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProduct(Product product) {
        productName.setText(product.name);
        Glide.with(getApplicationContext())
                .load(product.images.get(0))
                .into(productImage);
        productDescription.setText(product.description);
        productPrice.setText("Price: $" + product.price);
        categoryName.setText("Category: " + product.categoryDto.name + " > " + product.categoryDto.parentCategoryName);

        attributesContainer.removeAllViews();

        for (Attribute attr : product.attributes) {
            TextView textView = new TextView(this);
            textView.setText(attr.name + ": " + attr.value);
            attributesContainer.addView(textView);
        }
    }
}