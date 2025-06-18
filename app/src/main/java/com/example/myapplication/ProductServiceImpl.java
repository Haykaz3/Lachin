package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;

import com.example.myapplication.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class ProductServiceImpl implements ProductService {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String BASE_URL = "https://tr4m9tkv-7207.euw.devtunnels.ms/";

    @Override
    public void getById(int productId, ProductServiceCallback callback) {
        String url = BASE_URL + "products/" + productId;  // Using int productId in URL

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.onFailure("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Product product = gson.fromJson(response.body().string(), Product.class);
                    handler.post(() -> callback.onProductFetched(product));
                } else {
                    handler.post(() -> callback.onFailure("Error: " + response.code()));
                }
            }
        });
    }

    @Override
    public void getAll(ProductServiceCallback callback) {
        String url = BASE_URL + "products";  // Assuming the endpoint to fetch all products is `/products`

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> callback.onFailure("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Read response body
                        String json = response.body().string();

                        // Parse JSON into List<Product> using TypeToken
                        Type productListType = new TypeToken<List<Product>>() {}.getType();
                        List<Product> products = gson.fromJson(json, productListType);

                        // Post result to callback on main thread
                        handler.post(() -> callback.onProductsFetched(products));
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(() -> callback.onFailure("Parsing failed: " + e.getMessage()));
                    }
                } else {
                    handler.post(() -> callback.onFailure("Server error: " + response.code()));
                }
            }

        });
    }

    @Override
    public void addProduct(ProductDTO product, List<File> images, ProductAddCallback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // Add product fields
        builder.addFormDataPart("Name", product.name);
        builder.addFormDataPart("Description", product.description);
        builder.addFormDataPart("Price", String.valueOf(product.price));
        builder.addFormDataPart("CategoryId", String.valueOf(product.categoryId));
        builder.addFormDataPart("City", product.city);
        builder.addFormDataPart("Region", product.region);
        builder.addFormDataPart("Status", product.status);
        builder.addFormDataPart("CreatedByUserId", product.createdByUserId);

        // Add attributes JSON
        String attributesJson = gson.toJson(product.attributes); // Assuming List<AttributeDto>
        builder.addFormDataPart("AttributesJson", attributesJson);

        // Add image files
        for (int i = 0; i < images.size(); i++) {
            File image = images.get(i);
            builder.addFormDataPart("Images", image.getName(),
                    RequestBody.create(image, MediaType.parse("image/*")));
        }

        RequestBody requestBody = builder.build();
        String url = BASE_URL + "products";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed: " + response.code());
                }
            }
        });
    }
}
