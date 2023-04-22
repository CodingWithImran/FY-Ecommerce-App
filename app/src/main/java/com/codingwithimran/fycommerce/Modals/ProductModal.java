package com.codingwithimran.fycommerce.Modals;

import java.io.Serializable;

public class ProductModal implements Serializable {
    String  product_img, description, product_video, name, rating, productNumber;
    int price;

    public ProductModal(String product_img, String description, String name, String rating, String productNumber, int price) {
        this.product_img = product_img;
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.productNumber = productNumber;
        this.price = price;
    }

    public ProductModal(String product_img, String description, String name, String rating, int price) {
        this.product_img = product_img;
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.price = price;
    }

    public ProductModal() {
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProduct_video() {
        return product_video;
    }

    public void setProduct_video(String product_video) {
        this.product_video = product_video;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
