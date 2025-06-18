package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        int productId = getIntent().getIntExtra("product_id", -1);  // This is an int now
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
        productPrice.setText("$" + product.price);
        categoryName.setText("Category: " + product.categoryDto.name + " > " + product.categoryDto.parentCategoryName);

        attributesContainer.removeAllViews();

        for (Attribute attr : product.attributes) {
            // Horizontal layout for name and value
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            rowLayout.setPadding(0, 16, 0, 16);

            // Attribute name (left-aligned)
            TextView nameView = new TextView(this);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f // weight
            ));
            nameView.setText(attr.name);
            nameView.setTextSize(16);

            // Attribute value (right-aligned)
            TextView valueView = new TextView(this);
            valueView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f // weight
            ));
            valueView.setText(attr.value);
            valueView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            valueView.setTextSize(16);

            // Add name and value to row
            rowLayout.addView(nameView);
            rowLayout.addView(valueView);

            // Divider
            View divider = new View(this);
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1
            );
            divider.setLayoutParams(dividerParams);
            divider.setBackgroundColor(0xFFCCCCCC); // light gray

            // Add to container
            attributesContainer.addView(rowLayout);
            attributesContainer.addView(divider);
        }

    }
}