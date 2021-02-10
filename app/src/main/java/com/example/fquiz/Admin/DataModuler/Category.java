package com.example.fquiz.Admin.DataModuler;

public class Category {
    String id;
    String categoryName;

    public Category(String id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }


    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
