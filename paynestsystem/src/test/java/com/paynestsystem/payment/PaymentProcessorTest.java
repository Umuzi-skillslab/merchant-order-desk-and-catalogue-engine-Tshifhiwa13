package com.paynestsystem.payment;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentProcessorTest {

    private final PaymentProcessor processor = new PaymentProcessor();

    @Test
    void successfulPaymentPrintsRailAmountAndSettlement() {
        String output = captureOutput(() -> assertTrue(processor.processPayment(new CardPayment(), 2510)));

        assertTrue(output.contains("[CARD]"));
        assertTrue(output.contains("Payment successful via CARD"));
        assertTrue(output.contains("Amount: R2510,00"));
        assertTrue(output.contains("Settles: immediate"));
    }

    @Test
    void eftPaymentPrintsBankSettlementAndReference() {
        String output = captureOutput(() -> assertTrue(processor.processPayment(new EftPayment(), 2510)));

        assertTrue(output.contains("Payment successful via EFT"));
        assertTrue(output.contains("Settles: 1-2 business days (reference: EFT-"));
    }

    @Test
    void failedPaymentPrintsFailureNarrative() {
        String output = captureOutput(() -> assertFalse(processor.processPayment(new WalletPayment(100), 2510)));

        assertTrue(output.contains("Payment failed via WALLET"));
        assertFalse(output.contains("Payment successful"));
    }

    @Test
    void nullPaymentMethodIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> processor.processPayment(null, 2510));
    }

    private static String captureOutput(Runnable action) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(buffer));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString();
    }
}

