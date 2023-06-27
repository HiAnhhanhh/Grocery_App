package com.example.grocery_app.models;

public class OrderUserModels {

    String orderId, orderCost, orderTime, orderBy, orderTo, orderStatus;

    public OrderUserModels(String orderId, String orderCost, String orderTime, String orderBy, String orderTo, String orderStatus) {
        this.orderId = orderId;
        this.orderCost = orderCost;
        this.orderTime = orderTime;
        this.orderBy = orderBy;
        this.orderTo = orderTo;
        this.orderStatus = orderStatus;
    }

    public OrderUserModels() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderTo() {
        return orderTo;
    }

    public void setOrderTo(String orderTo) {
        this.orderTo = orderTo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
