package com.paynestsystem.payment;

import com.paynestsystem.util.RandFormat;

import java.math.BigDecimal;

/**
 * Payment by a digital wallet.
 * Settles instantly and can be refunded.
 */
public class WalletPayment extends PaymentMethod implements InstantSettlement, Refundable {

    /** Balance used when the demo does not specify one. */
    public static final double DEFAULT_BALANCE = 50_000.00;

    private BigDecimal balance;

    /** Creates a wallet with the default demo balance of R50 000. */
    public WalletPayment() {
        this(DEFAULT_BALANCE);
    }

    /**
     * Creates a wallet with a specific starting balance.
     *
     * @param startingBalance the balance in Rand (zero or more)
     */
    public WalletPayment(double startingBalance) {
        if (!Double.isFinite(startingBalance) || startingBalance < 0) {
            throw new IllegalArgumentException("Wallet balance cannot be negative");
        }
        this.balance = BigDecimal.valueOf(startingBalance);
    }

    @Override
    protected boolean doProcess(double amount) {
        BigDecimal charge = BigDecimal.valueOf(amount);
        if (charge.compareTo(balance) > 0) {
            System.out.println("[WALLET] Declined: balance R" + RandFormat.format(balance)
                    + " is lower than " + RandFormat.rand(amount));
            return false;
        }
        balance = balance.subtract(charge);
        System.out.println("[WALLET] Debiting wallet " + RandFormat.rand(amount)
                + " (remaining balance R" + RandFormat.format(balance) + ")");
        return true;
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    @Override
    public boolean refund(double amount) {
        if (!isValidAmount(amount)) {
            return false;
        }
        balance = balance.add(BigDecimal.valueOf(amount));
        System.out.println("[WALLET] Refunded " + RandFormat.rand(amount) + " into the wallet");
        return true;
    }

    /**
     * @return the current wallet balance in Rand
     */
    public double getBalance() {
        return balance.doubleValue();
    }
}

