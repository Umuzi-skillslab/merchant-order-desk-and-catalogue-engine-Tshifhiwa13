package com.paynestsystem.payment;

/**
 * The shared amount rule for every payment rail, written once.
 * <p>
 * Every rail calls {@link #validate(String, double)} at the start of
 * {@code processPayment}, and refundable rails call {@link #isValid(double)}
 * before refunding.
 */
public final class AmountValidator {

    private AmountValidator() {
        // helper class: no instances
    }

    /**
     * Rejects zero, negative and non-numeric (NaN or infinite) amounts.
     *
     * @param amount the amount to check
     * @return {@code true} if the amount can be charged or refunded
     */
    public static boolean isValid(double amount) {
        return Double.isFinite(amount) && amount > 0;
    }

    /**
     * Checks the amount and prints a rejection message for the rail if it is invalid.
     *
     * @param paymentType the rail's label, e.g. {@code CARD}
     * @param amount      the amount to charge
     * @return {@code true} if the rail may go ahead with the payment
     */
    public static boolean validate(String paymentType, double amount) {
        if (isValid(amount)) {
            return true;
        }
        System.out.println("[" + paymentType + "] Rejected: amount must be greater than R0,00");
        return false;
    }
}
