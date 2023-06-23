package com.example.grocery_app;

public class Constants {

    public static final String [] options ={
            "Beverages",
            "Beauty and personal Care",
            "Baby Kid",
            "Cooking Need",
            "Frozen Food",
            "Biscuits Snake and Chocolate",
            "Breakfast & Dairy",
            "Fruits",
            "Pet Cate",
            "Pharmacy",
            "Vegetable",
            "Others"
    };

    public static final String [] optionsCategories ={
            "All",
            "Beverages",
            "Beauty and personal Care",
            "Baby Kid",
            "Cooking Need",
            "Frozen Food",
            "Biscuits Snake and Chocolate",
            "Breakfast & Dairy",
            "Fruits",
            "Pet Cate",
            "Pharmacy",
            "Vegetable",
            "Others"
    };


    public static class ProductModels {

        public ProductModels() {
        }

        String description, productImage ,category, originalPrice, quantity, discountedPrice, discountedNote, title, timestamp, uid, productId, discountAvailable;

        public ProductModels(String description, String productImage, String category, String originalPrice, String quantity, String discountedPrice, String discountedNote, String title,
                             String timestamp, String uid, String productId, String discountAvailable) {
            this.description = description;
            this.productImage = productImage;
            this.category = category;
            this.originalPrice = originalPrice;
            this.quantity = quantity;
            this.discountedPrice = discountedPrice;
            this.discountedNote = discountedNote;
            this.title = title;
            this.timestamp = timestamp;
            this.uid = uid;
            this.productId = productId;
            this.discountAvailable = discountAvailable;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDiscountedPrice() {
            return discountedPrice;
        }

        public void setDiscountedPrice(String discountedPrice) {
            this.discountedPrice = discountedPrice;
        }

        public String getDiscountedNote() {
            return discountedNote;
        }

        public void setDiscountedNote(String discountedNote) {
            this.discountedNote = discountedNote;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getDiscountAvailable() {
            return discountAvailable;
        }

        public void setDiscountAvailable(String discountAvailable) {
            this.discountAvailable = discountAvailable;
        }
    }
}
