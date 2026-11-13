package com.paynestsystem.service;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.OrderStatus;
import com.paynestsystem.domain.Product;
import com.paynestsystem.payment.BuyNowPayLaterPayment;
import com.paynestsystem.payment.CardPayment;
import com.paynestsystem.payment.EftPayment;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.PaymentProcessor;
import com.paynestsystem.payment.WalletPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutServiceTest {

    private CheckoutService checkoutService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(new PaymentProcessor());
        customer = new Customer(1, "Tshifhiwa Makherana", "Fifi@example.org");
    }

    /** The Capstone 1 demo order: Hammer R150 + Drill R2000 + 3 x Nails R120 = R2510. */
    private Order capstoneOrder() {
        Order order = new Order(35, customer);
        order.addItem(new Product(1, "Hammer", BigDecimal.valueOf(150)), 1);
        order.addItem(new Product(2, "Electric Drill", BigDecimal.valueOf(2000)), 1);
        order.addItem(new Product(3, "Box of Nails", BigDecimal.valueOf(120)), 3);
        return order;
    }

    @Test
    void checkoutCompletesTheSameOrderWithEveryRail() {
        List<PaymentMethod> rails = List.of(
                new CardPayment(), new EftPayment(), new WalletPayment(), new BuyNowPayLaterPayment());

        for (PaymentMethod rail : rails) {
            Order order = capstoneOrder();
            String output = captureOutput(() -> assertTrue(checkoutService.checkout(order, rail)));

            assertEquals(OrderStatus.PAID, order.getStatus(), rail.getPaymentType());
            assertTrue(output.contains("Payment successful via " + rail.getPaymentType()));
            assertTrue(output.contains("Order #35 completed: R2510,00 paid via " + rail.getPaymentType()));
        }
    }

    @Test
    void amountChargedEqualsOrderTotal() {
        Order order = capstoneOrder();
        RecordingPayment rail = new RecordingPayment(true);

        captureOutput(() -> checkoutService.checkout(order, rail));

        assertEquals(1, rail.getCalls());
        assertEquals(order.calculateTotal().doubleValue(), rail.getChargedAmount(), 0.0);
        assertEquals(2510.00, rail.getChargedAmount(), 0.0);
    }

    @Test
    void amountChargedEqualsRoundedTotalForDecimalPrices() {
        Order order = new Order(36, customer);
        order.addItem(new Product(4, "Screws", new BigDecimal("19.99")), 3);   // 59.97
        order.addItem(new Product(5, "Washer", new BigDecimal("0.10")), 3);    //  0.30
        RecordingPayment rail = new RecordingPayment(true);

        captureOutput(() -> checkoutService.checkout(order, rail));

        assertEquals(60.27, rail.getChargedAmount(), 0.0);
    }

    @Test
    void declinedPaymentLeavesOrderOpenSoItCanBeRetried() {
        Order order = capstoneOrder();

        String output = captureOutput(() -> assertFalse(checkoutService.checkout(order, new WalletPayment(500))));
        assertEquals(OrderStatus.OPEN, order.getStatus());
        assertTrue(output.contains("Payment failed via WALLET"));
        assertTrue(output.contains("NOT completed"));

        captureOutput(() -> assertTrue(checkoutService.checkout(order, new CardPayment())));
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void emptyOrderCannotBeCheckedOutAndRailIsNeverCalled() {
        Order empty = new Order(37, customer);
        RecordingPayment rail = new RecordingPayment(true);

        assertThrows(IllegalStateException.class, () -> checkoutService.checkout(empty, rail));
        assertEquals(0, rail.getCalls());
    }

    @Test
    void paidOrderCannotBeChargedTwice() {
        Order order = capstoneOrder();
        captureOutput(() -> checkoutService.checkout(order, new CardPayment()));
        RecordingPayment secondRail = new RecordingPayment(true);

        assertThrows(IllegalStateException.class, () -> checkoutService.checkout(order, secondRail));
        assertEquals(0, secondRail.getCalls());
    }

    @Test
    void missingOrderOrPaymentMethodIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> checkoutService.checkout(null, new CardPayment()));
        assertThrows(IllegalArgumentException.class, () -> checkoutService.checkout(capstoneOrder(), null));
        assertThrows(IllegalArgumentException.class, () -> new CheckoutService(null));
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

