package com.fastmarket.fastmarket;

import java.util.Arrays;

/**
 * Created by Sebastian on 8/05/2016.
 */
public class Product {
    private String name;
    private byte[] image;
    private String categories;
    private float price;
    private int quantity;
    private String imageURL;
    private String code;

    public Product(String name, String categories, float price, int quantity, String code, byte[] image) {
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.quantity = quantity;
        this.code = code;
        this.image = image;
        this.imageURL = "";
    }

    public Product(String name, String categories, float price, int quantity, String imageURL, String code) {
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.code = code;
    }

    public Product(String name, byte[] image, float price, int quantity, String imageURL, String code) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCode() {
        return code;
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

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", categories='" + categories + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", imageURL='" + imageURL + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
