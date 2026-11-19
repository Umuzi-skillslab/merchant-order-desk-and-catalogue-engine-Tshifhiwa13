package com.paynestsystem.payment;

/**
 * Common parent of the settlement capability groups ({@link InstantSettlement}
 * and {@link BankSettlement}). Each group describes its own settlement, so
 * reporting code asks this one interface instead of checking every group.
 */
public interface Settlement {

    /**
     * How and when the merchant receives the money, for logs and receipts.
     *
     * @return a human-readable settlement description
     */
    String describeSettlement();
}
