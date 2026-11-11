package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Payment by Electronic Funds Transfer (bank transfer).
 * Settles through the bank in 1-2 business days and gets a bank reference.
 */
public class EftPayment extends PaymentMethod implements BankSettlement {

    /** Shared counter so every EFT gets its own reference: EFT-0001, EFT-0002, ... */
    private static final AtomicInteger NEXT_REFERENCE = new AtomicInteger(1);

    private String bankReference = "EFT-PENDING";

    @Override
    protected boolean doProcess(double amount) {
        bankReference = String.format("EFT-%04d", NEXT_REFERENCE.getAndIncrement());
        System.out.println("[EFT] Initiating bank transfer of " + RandFormat.rand(amount)
                + " (reference " + bankReference + ")");
        return true; // Simulated: the bank always accepts the transfer instruction.
    }

    @Override
    public String getPaymentType() {
        return "EFT";
    }

    /**
     * @return {@code EFT-PENDING} until a payment is processed, then the assigned reference
     */
    @Override
    public String getBankReference() {
        return bankReference;
    }
}
