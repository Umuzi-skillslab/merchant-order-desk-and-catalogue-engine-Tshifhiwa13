package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

/**
 * Processes a payment through any {@link PaymentMethod} and prints the
 * console narrative support staff use to replay demos.
 */
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

   
    private String describeSettlement(PaymentMethod method) {
        if (method instanceof BankSettlement bank) {
            return bank.settlementTime() + " (reference: " + bank.getBankReference() + ")";
        }
        if (method instanceof InstantSettlement instant) {
            return instant.settlementTime();
        }
        return "not specified";
    }
}

