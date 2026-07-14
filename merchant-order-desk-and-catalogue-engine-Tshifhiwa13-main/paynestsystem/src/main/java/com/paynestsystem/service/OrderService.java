package com.paynestsystem.service;

import com.paynestsystem.domain.*;

public class OrderService {

    public void printSummary(Order order) {
        if (order == null) {
            System.out.println("No order details available.");
            return;
        }

        System.out.println("=======================================");
        System.out.println("************* PAYMENT INVOICE *********");
        System.out.println("=======================================");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Customer: " + order.getCustomer().getName());
        System.out.println("Email: " + order.getCustomer().getEmail());
        System.out.println("=======================================");
        System.out.printf("%-20s %-10s %s%n", "Product", "Qty", "Subtotal");
        System.out.println("=======================================");

        for (OrderItem item : order.getItems()) {
            System.out.printf("%-20s %-10d %.2f%n",
                item.getProduct().getName(),
                item.getQuantity(),
                item.calculateTotal());
        }

        System.out.println("=======================================");
        System.out.printf("%-31s %.2f%n", "Total Amount:", order.calculateTotal());
        System.out.println("=======================================.");
    }
}
