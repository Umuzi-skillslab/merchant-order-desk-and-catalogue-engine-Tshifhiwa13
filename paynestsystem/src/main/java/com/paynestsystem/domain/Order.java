package com.paynestsystem.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An order placed by a customer: identity, owned line items, total and status.
 * The order knows nothing about payment rails. Checkout is orchestrated by
 * {@code CheckoutService}, so adding a new rail never touches this class.
 */
public class Order {

    private final int id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.OPEN;

    public Order(int id, Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("An order belongs to a customer");
        }
        this.id = id;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * @return a read-only view of the line items
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Adds a product line. {@link OrderItem}'s constructor rejects a null
     * product or a quantity of zero or less.
     *
     * @throws IllegalStateException if the order has already been paid
     */
    public void addItem(Product product, int quantity) {
        if (status != OrderStatus.OPEN) {
            throw new IllegalStateException("Order #" + id + " is " + status + "; items can no longer be added");
        }
        items.add(new OrderItem(product, quantity));
    }

    /**
     * Grand total: the sum of every line subtotal, rounded once to cents
     * (half-up). This is the single source of truth for both the invoice and
     * the amount charged at checkout. An empty order totals {@code 0.00}.
     *
     * @return the total in Rand, with two decimals
     */
    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.calculateTotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Marks the order as paid and locks it. Called by checkout only after the
     * payment rail reported success.
     *
     * @throws IllegalStateException if the order is empty or already paid
     */
    public void markPaid() {
        if (items.isEmpty()) {
            throw new IllegalStateException("An empty order cannot be paid");
        }
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("Order #" + id + " has already been paid");
        }
        status = OrderStatus.PAID;
    }
}
