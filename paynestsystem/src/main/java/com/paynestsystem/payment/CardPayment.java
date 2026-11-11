package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

/**
 * Payment by credit or debit card.
 * Settles instantly and can be refunded.
 */
public class CardPayment extends PaymentMethod implements InstantSettlement, Refundable {

    @Override
    protected boolean doProcess(double amount) {
        System.out.println("[CARD] Authorising card payment of " + RandFormat.rand(amount));
        return true; // Simulated: the card network always approves in this capstone.
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }

    @Override
    public boolean refund(double amount) {
        if (!isValidAmount(amount)) {
            return false;
        }
        System.out.println("[CARD] Refunding " + RandFormat.rand(amount) + " to the card");
        return true;
    }
}
