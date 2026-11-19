package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

/**
 * Payment by credit or debit card.
 * Settles instantly and can be refunded.
 */
public class CardPayment implements PaymentMethod, InstantSettlement, Refundable {

    @Override
    public boolean processPayment(double amount) {
        if (!AmountValidator.validate(getPaymentType(), amount)) {
            return false;
        }
        System.out.println("[CARD] Authorising card payment of " + RandFormat.rand(amount));
        return true; // Simulated: the card network always approves in this capstone.
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }

    @Override
    public boolean refund(double amount) {
        if (!AmountValidator.isValid(amount)) {
            return false;
        }
        System.out.println("[CARD] Refunding " + RandFormat.rand(amount) + " to the card");
        return true;
    }
}
