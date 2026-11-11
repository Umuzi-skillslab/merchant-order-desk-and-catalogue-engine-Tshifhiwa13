package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Buy now, pay later: the customer pays in four instalments while the BNPL
 * provider pays the merchant upfront, so for the merchant it settles instantly.
 */
public class BuyNowPayLaterPayment extends PaymentMethod implements InstantSettlement {

    /** Number of instalments the customer pays. */
    public static final int INSTALMENTS = 4;

    /** Highest order total the BNPL provider approves in the simulation. */
    public static final double APPROVAL_LIMIT = 20_000.00;

    @Override
    protected boolean doProcess(double amount) {
        if (amount > APPROVAL_LIMIT) {
            System.out.println("[BNPL] Declined: " + RandFormat.rand(amount)
                    + " is above the approval limit of " + RandFormat.rand(APPROVAL_LIMIT));
            return false;
        }
        BigDecimal instalment = BigDecimal.valueOf(amount)
                .divide(BigDecimal.valueOf(INSTALMENTS), 2, RoundingMode.HALF_UP);
        System.out.println("[BNPL] Approved " + RandFormat.rand(amount) + " as "
                + INSTALMENTS + " instalments of R" + RandFormat.format(instalment));
        return true;
    }

    @Override
    public String getPaymentType() {
        return "BNPL";
    }
}
