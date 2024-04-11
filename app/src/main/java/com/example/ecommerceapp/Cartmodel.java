package com.example.ecommerceapp;

public class Cartmodel {
    String productDate;
    String productName;
    String productPrice;
    String productTime;
    int totalPrice;
    String totalQuantity;

    public Cartmodel() {
    }

    public Cartmodel(String productDate, String productName, String productPrice, String productTime, int totalPrice, String totalQuantity) {
        this.productDate = productDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productTime = productTime;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public String getProductDate() { return productDate; }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductTime() {
        return productTime;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }
}
