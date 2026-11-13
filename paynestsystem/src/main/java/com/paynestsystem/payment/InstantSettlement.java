package com.paynestsystem.payment;

/**
 * Capability group: rails where the merchant receives the money immediately.
 * Card, wallet and buy-now-pay-later belong here; EFT does not.
 */
public interface InstantSettlement {

    /**
     * When the money lands with the merchant.
     *
     * @return a human-readable settlement time
     */
    default String settlementTime() {
        return "immediate";
    }
}
