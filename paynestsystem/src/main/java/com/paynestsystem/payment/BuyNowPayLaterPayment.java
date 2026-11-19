package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Buy now, pay later: the customer pays in four instalments while the BNPL
 * provider pays the merchant upfront, so for the merchant it settles instantly.
 */
public class BuyNowPayLaterPayment implements PaymentMethod, InstantSettlement {

    /** Number of instalments the customer pays. */
    public static final int INSTALMENTS = 4;

    /** Highest order total the BNPL provider approves in the simulation. */
    public static final double APPROVAL_LIMIT = 20_000.00;

    @Override
    public boolean processPayment(double amount) {
        if (!AmountValidator.validate(getPaymentType(), amount)) {
            return false;
        }
        if (amount > APPROVAL_LIMIT) {
            System.out.println("[BNPL] Declined: " + RandFormat.rand(amount)
                    + " is above the approval limit of " + RandFormat.rand(APPROVAL_LIMIT));
            return false;
        }
        StringJoiner plan = new StringJoiner(" + ");
        for (BigDecimal instalment : instalmentsFor(amount)) {
            plan.add("R" + RandFormat.format(instalment));
        }
        System.out.println("[BNPL] Approved " + RandFormat.rand(amount) + " as "
                + INSTALMENTS + " instalments: " + plan);
        return true;
    }

    /**
     * Splits a total into {@link #INSTALMENTS} instalments that add up to exactly
     * the total. The split is done in whole cents; leftover cents go to the
     * first instalments, so R0,31 becomes R0,08 + R0,08 + R0,08 + R0,07.
     *
     * @param amount the order total in Rand
     * @return the instalments, each with two decimals
     */
    public static List<BigDecimal> instalmentsFor(double amount) {
        long totalCents = BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .movePointRight(2)
                .longValueExact();
        long baseCents = totalCents / INSTALMENTS;
        long leftoverCents = totalCents % INSTALMENTS;

        List<BigDecimal> instalments = new ArrayList<>();
        for (int i = 0; i < INSTALMENTS; i++) {
            long cents = baseCents + (i < leftoverCents ? 1 : 0);
            instalments.add(BigDecimal.valueOf(cents, 2));
        }
        return instalments;
    }

    @Override
    public String getPaymentType() {
        return "BNPL";
    }
}
