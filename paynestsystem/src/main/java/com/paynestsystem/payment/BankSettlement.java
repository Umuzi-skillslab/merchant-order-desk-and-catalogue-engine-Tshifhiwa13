package com.paynestsystem.payment;

/**
 * Capability group: rails that go through the banking system and settle later.
 */
public interface BankSettlement {

    /**
     * When the money lands with the merchant.
     *
     * @return a human-readable settlement time
     */
    default String settlementTime() {
        return "1-2 business days";
    }

    /**
     * The bank's reference for this payment.
     *
     * @return the bank reference
     */
    String getBankReference();
}