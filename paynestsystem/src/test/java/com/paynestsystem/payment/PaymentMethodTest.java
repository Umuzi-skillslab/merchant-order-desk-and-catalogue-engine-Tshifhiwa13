package com.paynestsystem.payment;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentMethodTest {

    @Test
    void checkoutCanUseDifferentPaymentMethods() {
        PaymentMethod card = new CardPayment();
        PaymentMethod wallet = new WalletPayment();

        assertTrue(card.processPayment(12400));
        assertTrue(wallet.processPayment(12400));
        assertEquals("CARD", card.getPaymentType());
        assertEquals("WALLET", wallet.getPaymentType());
    }

    @Test
    void everyRailHasAStableLabel() {
        assertEquals("CARD", new CardPayment().getPaymentType());
        assertEquals("EFT", new EftPayment().getPaymentType());
        assertEquals("WALLET", new WalletPayment().getPaymentType());
        assertEquals("BNPL", new BuyNowPayLaterPayment().getPaymentType());
    }

    @Test
    void sharedValidationRejectsZeroNegativeAndNonNumericAmountsOnEveryRail() {
        List<PaymentMethod> rails = List.of(
                new CardPayment(), new EftPayment(), new WalletPayment(), new BuyNowPayLaterPayment());

        for (PaymentMethod rail : rails) {
            assertFalse(rail.processPayment(0), rail.getPaymentType() + " accepted R0");
            assertFalse(rail.processPayment(-50), rail.getPaymentType() + " accepted a negative amount");
            assertFalse(rail.processPayment(Double.NaN), rail.getPaymentType() + " accepted NaN");
        }
    }

    @Test
    void walletDeclinesWhenBalanceIsTooLowAndKeepsBalance() {
        WalletPayment wallet = new WalletPayment(500);

        assertFalse(wallet.processPayment(2510));
        assertEquals(500.0, wallet.getBalance(), 0.0001);
    }

    @Test
    void walletDebitsAndRefundsBalance() {
        WalletPayment wallet = new WalletPayment(3000);

        assertTrue(wallet.processPayment(2510));
        assertEquals(490.0, wallet.getBalance(), 0.0001);

        assertTrue(wallet.refund(2510));
        assertEquals(3000.0, wallet.getBalance(), 0.0001);
    }

    @Test
    void walletRejectsNegativeStartingBalance() {
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(-1));
    }

    @Test
    void eftReceivesABankReferenceOnlyAfterProcessing() {
        EftPayment eft = new EftPayment();
        assertEquals("EFT-PENDING", eft.getBankReference());

        assertTrue(eft.processPayment(2510));
        assertTrue(eft.getBankReference().matches("EFT-\\d{4}"));
    }

    @Test
    void bnplDeclinesAmountsAboveItsApprovalLimit() {
        PaymentMethod bnpl = new BuyNowPayLaterPayment();

        assertTrue(bnpl.processPayment(2510));
        assertFalse(bnpl.processPayment(BuyNowPayLaterPayment.APPROVAL_LIMIT + 1));
    }

    @Test
    void railsJoinTheRightCapabilityGroups() {
        assertTrue(new CardPayment() instanceof InstantSettlement);
        assertTrue(new CardPayment() instanceof Refundable);
        assertTrue(new WalletPayment() instanceof InstantSettlement);
        assertTrue(new WalletPayment() instanceof Refundable);
        assertTrue(new EftPayment() instanceof BankSettlement);
        assertFalse(new EftPayment() instanceof Refundable);
        assertTrue(new BuyNowPayLaterPayment() instanceof InstantSettlement);
    }
}

