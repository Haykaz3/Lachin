package com.example.myapplication;

import java.util.List;

public interface AttributeService {
    void getAttributesByCategory(int categoryId, AttributeCallback callback);

    interface AttributeCallback {
        void onAttributesFetched(List<AttributeDefinition> attributes);
        void onFailure(String error);
    }

}
