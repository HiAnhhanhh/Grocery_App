package com.example.grocery_app.models;

public class OrderItemsModels {
    String cost,name,price, productId, quantity, unit;

    public OrderItemsModels(String cost, String name, String price, String productId, String quantity, String unit) {
        this.cost = cost;
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.quantity = quantity;
        this.unit = unit;
    }

    public OrderItemsModels() {
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
