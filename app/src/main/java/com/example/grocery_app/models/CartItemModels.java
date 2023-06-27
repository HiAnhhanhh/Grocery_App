package com.example.grocery_app.models;

public class CartItemModels {

    String itemName, itemPrice, itemPriceEach, itemProductId, itemQuantity, timestamp, itemId;

    public CartItemModels() {
    }

    public CartItemModels(String itemName, String itemPrice, String itemPriceEach, String itemProductId,
                          String itemQuantity, String timestamp, String itemId) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPriceEach = itemPriceEach;
        this.itemProductId = itemProductId;
        this.itemQuantity = itemQuantity;
        this.timestamp = timestamp;
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPriceEach() {
        return itemPriceEach;
    }

    public void setItemPriceEach(String itemPriceEach) {
        this.itemPriceEach = itemPriceEach;
    }

    public String getItemProductId() {
        return itemProductId;
    }

    public void setItemProductId(String itemProductId) {
        this.itemProductId = itemProductId;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}


