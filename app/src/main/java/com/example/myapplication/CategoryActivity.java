package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private CategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Vertical list

        // Initialize CategoryService
        categoryService = new CategoryServiceImpl();

        // Fetch categories
        categoryService.getCategories(new CategoryService.CategoryServiceCallback() {
            @Override
            public void onCategoriesFetched(List<Category> categories) {
                // Set the adapter once categories are fetched
                categoryAdapter = new CategoryAdapter(CategoryActivity.this, categories);
                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String error) {
                // Show error message
                Toast.makeText(CategoryActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}