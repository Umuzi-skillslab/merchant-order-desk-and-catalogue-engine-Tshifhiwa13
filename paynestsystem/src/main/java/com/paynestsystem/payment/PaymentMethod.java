package com.paynestsystem.payment;

public abstract class PaymentMethod {

    public final boolean processPayment(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println("[" + getPaymentType() + "] Rejected: amount must be greater than R0,00");
            return false;
        }
        return doProcess(amount);
    }

    /**
     * Shared validation, written once. Rejects zero, negative and
     * non-numeric (NaN or infinite) amounts.
     *
     * @param amount the amount to check
     * @return {@code true} if the amount can be charged
     */
    protected boolean isValidAmount(double amount) {
        return Double.isFinite(amount) && amount > 0;
    }
    protected abstract boolean doProcess(double amount);

    public abstract String getPaymentType();
}
