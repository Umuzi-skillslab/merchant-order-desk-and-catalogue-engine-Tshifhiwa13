package com.paynestsystem.domain;

import java.util.ArrayList;
import java.util.List;


public class Order {

    private final int id;
    private final Customer customer;
    private final List<OrderItem> items;

    public  Order(int id, Customer customer) {
        if (id <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than zero.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        if (product == null || quantity <=0) {
            throw new IllegalArgumentException("Product cannot be null and quantity must be greater than zero.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        OrderItem orderItem = new OrderItem(product, quantity);
        items.add(orderItem);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.calculateTotal();
        }
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getId() {
        return id;
    }

 public List<OrderItem> getItems() {
        return items;
    }
}
