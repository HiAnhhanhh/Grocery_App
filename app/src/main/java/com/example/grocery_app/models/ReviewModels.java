package com.example.grocery_app.models;

public class ReviewModels {
    String review, reviewBy, reviewTo, rating, timestamp ;

    public ReviewModels() {
    }

    public ReviewModels(String review, String reviewBy, String reviewTo, String rating, String timestamp) {
        this.review = review;
        this.reviewBy = reviewBy;
        this.reviewTo = reviewTo;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy) {
        this.reviewBy = reviewBy;
    }

    public String getReviewTo() {
        return reviewTo;
    }

    public void setReviewTo(String reviewTo) {
        this.reviewTo = reviewTo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
