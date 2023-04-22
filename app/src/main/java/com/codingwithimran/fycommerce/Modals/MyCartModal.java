package com.codingwithimran.fycommerce.Modals;

public class MyCartModal {
    String currentDate;
    String currentTime;
    String ProductName;
    String ProductPrice;
    String ProductId;
    int totalPrice;
    int Quantity;

    public MyCartModal() {
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public MyCartModal(String currentDate, String currentTime, String productName, String productPrice, int totalPrice, int quantity) {
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.ProductName = productName;
        this.ProductPrice = productPrice;
        this.totalPrice = totalPrice;
        this.Quantity = quantity;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
