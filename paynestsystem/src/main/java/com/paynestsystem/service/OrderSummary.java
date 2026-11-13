package com.paynestsystem.service;

import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.OrderItem;
import com.paynestsystem.util.RandFormat;

/**
 * Presentation only: prints an order as an invoice. The total comes from
 * {@link Order#calculateTotal()}, the same figure checkout charges.
 */
public class OrderSummary {

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
        System.out.println("Status: " + order.getStatus());
        System.out.println("=======================================");
        System.out.printf("%-20s %-10s %s%n", "Product", "Qty", "Subtotal");
        System.out.println("=======================================");

        for (OrderItem item : order.getItems()) {
            System.out.printf("%-20s %-10d %s%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    RandFormat.format(item.calculateTotal()));
        }

        System.out.println("=======================================");
        System.out.printf("%-31s %s%n", "Total Amount:", RandFormat.format(order.calculateTotal()));
        System.out.println("=======================================");
    }
}

