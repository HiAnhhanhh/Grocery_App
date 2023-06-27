package com.example.grocery_app.models;

public class ProductUserModels {

    public ProductUserModels() {
    }

    String category, description, discountAvailable, discountedNote, discountedPrice, originalPrice, productId, productImage, quantity, timestamp, title, uid;

    public ProductUserModels(String category, String description, String discountAvailable, String discountedNote, String discountedPrice, String originalPrice, String productId,
                             String productImage, String quantity, String timestamp, String title, String uid) {
        this.category = category;
        this.description = description;
        this.discountAvailable = discountAvailable;
        this.discountedNote = discountedNote;
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
        this.productId = productId;
        this.productImage = productImage;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.title = title;
        this.uid = uid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getDiscountedNote() {
        return discountedNote;
    }

    public void setDiscountedNote(String discountedNote) {
        this.discountedNote = discountedNote;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
