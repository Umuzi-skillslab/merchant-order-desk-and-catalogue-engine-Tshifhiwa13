package com.paynestsystem.service;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.Product;

public class OrderService {

    private final OrderSummary orderSummary;

    public OrderService(OrderSummary orderSummary) {
        if (orderSummary == null) {
            throw new IllegalArgumentException("An order summary printer is required");
        }
        this.orderSummary = orderSummary;
    }

    public Order createOrder(int orderId, Customer customer) {
        return new Order(orderId, customer);
    }

    public void addItem(Order order, Product product, int quantity) {
        order.addItem(product, quantity);
    }

    public void printInvoice(Order order) {
        orderSummary.printSummary(order);
    }
}