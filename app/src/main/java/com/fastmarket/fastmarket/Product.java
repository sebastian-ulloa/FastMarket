package com.fastmarket.fastmarket;

/**
 * Created by Sebastian on 8/05/2016.
 */
public class Product {
    private String name;
    private String image;
    private String categories;
    private float price;
    private int quantity;

    public Product(String image, String categories, float price, String name, int quantity) {
        this.image = image;
        this.categories = categories;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getCategories() {
        return categories;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
