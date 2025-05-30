package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Product;
import com.example.myapplication.Attribute;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ProductActivity extends AppCompatActivity {

    private TextView productName, productDescription, productPrice, categoryName;
    private LinearLayout attributesContainer;

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    private String url = "https://bdqcjjh9-7207.euw.devtunnels.ms/product/4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        categoryName = findViewById(R.id.categoryName);
        attributesContainer = findViewById(R.id.attributesContainer);

        fetchProduct();
    }

    private void fetchProduct() {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final Product product = gson.fromJson(response.body().string(), Product.class);

                    runOnUiThread(() -> showProduct(product));
                }
            }
        });
    }

    private void showProduct(Product product) {
        productName.setText(product.name);
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