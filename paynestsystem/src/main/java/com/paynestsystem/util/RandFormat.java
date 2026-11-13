package com.paynestsystem.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * One place that decides how Rand amounts look in console output.
 * <p>
 *The payment rails and checkout narrative reuse the same format so the invoice total and the amount
 * charged can be compared character for character.
 */
public final class RandFormat {

    private RandFormat() {
        // utility class: no instances
    }

    /**
     * Formats an amount with two decimals and a comma separator, e.g. {@code 2510,00}.
     *
     * @param amount the amount to format
     * @return the formatted amount without the currency symbol
     */
    public static String format(BigDecimal amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("0.00", symbols);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(amount);
    }

    /**
     * Formats a payment amount with the Rand symbol, e.g. {@code R2510,00}.
     *
     * @param amount the amount passed to a payment rail
     * @return the formatted amount with the currency symbol
     */
    public static String rand(double amount) {
        return "R" + format(BigDecimal.valueOf(amount));
    }
}