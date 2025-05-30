package com.example.myapplication;

import java.util.List;

public class Product {
    public int id;
    public String name;
    public String description;
    public double price;
    public int categoryId;
    public Category categoryDto;
    public String createdByUserId;
    public List<Attribute> attributes;
}
