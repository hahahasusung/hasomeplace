package com.example.finalproject_1;

import java.util.List;

public class Order {
    private String orderType;
    private String orderId;
    private List<MenuItem> items;
    private int totalPrice;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String orderId, String orderType, List<MenuItem> items, int totalPrice) {
        this.orderType = orderType;
        this.orderId = orderId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNumber() {
        return orderId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderId = orderNumber;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}