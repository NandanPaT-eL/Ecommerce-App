package com.example.ecommerceapp;

public class Cartmodel {
    String productDate;
    String productName;
    String productPrice;
    String productTime;
    int totalPrice;
    String totalQuantity;
    String documentId;

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

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
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
