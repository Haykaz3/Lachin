package com.example.myapplication;

import java.util.List;

public interface CategoryService {
    void getCategories(CategoryServiceCallback callback);

    interface CategoryServiceCallback {
        void onCategoriesFetched(List<Category> categories);
        void onFailure(String error);
    }
}
