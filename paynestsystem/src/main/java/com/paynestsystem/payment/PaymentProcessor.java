package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

public class PaymentProcessor {

    /**
     * Charges the given amount on the given rail.
     *
     * @param method the rail chosen by the customer
     * @param amount the amount to charge (the order total)
     * @return {@code true} if the payment succeeded
     */
    public boolean processPayment(PaymentMethod method, double amount) {
        if (method == null) {
            throw new IllegalArgumentException("A payment method is required");
        }

        System.out.println("Processing payment via " + method.getPaymentType() + "...");
        boolean success = method.processPayment(amount);

        if (success) {
            System.out.println("Payment successful via " + method.getPaymentType());
            System.out.println("Amount: " + RandFormat.rand(amount));
            System.out.println("Settles: " + describeSettlement(method));
        } else {
            System.out.println("Payment failed via " + method.getPaymentType());
        }
        return success;
    }

    /**
     * Reporting only: each settlement group describes itself, so this method
     * never grows when a rail is added and never changes how a payment is processed.
     */
    private String describeSettlement(PaymentMethod method) {
        if (method instanceof Settlement settlement) {
            return settlement.describeSettlement();
        }
        return "not specified";
    }
}

