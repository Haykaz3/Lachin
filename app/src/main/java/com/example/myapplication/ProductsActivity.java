package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productService = new ProductServiceImpl(); // <-- Replace with your actual implementation

        // Fetch products asynchronously
        productService.getAll(new ProductService.ProductServiceCallback() {
            @Override
            public void onProductFetched(Product product) {
                // Not needed here
            }

            @Override
            public void onProductsFetched(List<Product> products) {
                // Update RecyclerView on UI thread
                runOnUiThread(() -> {
                    adapter = new ProductAdapter(products,ProductsActivity.this);
                    recyclerView.setAdapter(adapter);
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(ProductsActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}