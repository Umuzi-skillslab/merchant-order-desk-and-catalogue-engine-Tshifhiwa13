package com.paynestsystem.service;

import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.OrderStatus;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.PaymentProcessor;
import com.paynestsystem.util.RandFormat;

import java.math.BigDecimal;

/**
 * The one checkout funnel for every payment rail.
 * It accepts the {@link PaymentMethod} abstraction, never a concrete rail, and
 * contains no {@code if}/{@code switch} on payment type.
 */
public class CheckoutService {

    private final PaymentProcessor paymentProcessor;

    /**
     * @param paymentProcessor injected by the composition root ({@code PayNestApplication})
     */
    public CheckoutService(PaymentProcessor paymentProcessor) {
        if (paymentProcessor == null) {
            throw new IllegalArgumentException("A payment processor is required");
        }
        this.paymentProcessor = paymentProcessor;
    }

    /**
     * Checks out the order with the customer's chosen rail.
     *
     * @param order         the order to pay for
     * @param paymentMethod any payment rail
     * @return {@code true} if payment succeeded and the order is now paid;
     *         {@code false} if the rail declined (the order stays open)
     * @throws IllegalArgumentException if the order or payment method is missing
     * @throws IllegalStateException    if the order is empty or already paid
     */
    public boolean checkout(Order order, PaymentMethod paymentMethod) {
        if (order == null) {
            throw new IllegalArgumentException("An order is required for checkout");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("A payment method is required for checkout");
        }
        if (order.isEmpty()) {
            throw new IllegalStateException("Order #" + order.getId() + " has no items to check out");
        }
        if (order.getStatus() != OrderStatus.OPEN) {
            throw new IllegalStateException("Order #" + order.getId() + " has already been paid");
        }

        // Calculated once; the same figure is charged and printed.
        BigDecimal total = order.calculateTotal();
        String amount = "R" + RandFormat.format(total);

        System.out.println();
        System.out.println("=======================================");
        System.out.println("************* CHECKOUT ****************");
        System.out.println("=======================================");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Amount due: " + amount);
        System.out.println("Payment rail: " + paymentMethod.getPaymentType());
        System.out.println("---------------------------------------");

        boolean paid = paymentProcessor.processPayment(paymentMethod, total.doubleValue());

        System.out.println("---------------------------------------");
        if (paid) {
            order.markPaid();
            System.out.println("Order #" + order.getId() + " completed: " + amount
                    + " paid via " + paymentMethod.getPaymentType()
                    + ". Status: " + order.getStatus());
        } else {
            System.out.println("Order #" + order.getId() + " NOT completed: payment via "
                    + paymentMethod.getPaymentType() + " failed. Status: " + order.getStatus());
        }
        System.out.println("=======================================");
        return paid;
    }
}
