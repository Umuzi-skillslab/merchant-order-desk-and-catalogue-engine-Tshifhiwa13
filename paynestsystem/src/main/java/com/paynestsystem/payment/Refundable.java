package com.paynestsystem.payment;

public interface Refundable {

    /**
     * Refunds the given amount to the customer.
     *
     * @param amount the amount to refund (must be greater than zero)
     * @return {@code true} if the refund succeeded
     */
    boolean refund(double amount);
}
