package com.paynestsystem.domain;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    private final Customer customer = new Customer(1, "Tshifhiwa Makherana", "Fifi@example.org");
    private final Product hammer = new Product(1, "Hammer", BigDecimal.valueOf(150));

    @Test
    void calculateTotalAddsAllLineSubtotals() {
        Product drill = new Product(2, "Electric Drill", BigDecimal.valueOf(2000));
        Product nails = new Product(3, "Box of Nails", BigDecimal.valueOf(120));
        Order order = new Order(35, customer);

        order.addItem(hammer, 1);
        order.addItem(drill, 1);
        order.addItem(nails, 3);

        BigDecimal expectedTotal = BigDecimal.valueOf(150 + 2000 + 360);
        assertEquals(0, order.calculateTotal().compareTo(expectedTotal));
    }

    @Test
    void emptyOrderTotalsZero() {
        Order order = new Order(35, customer);

        assertTrue(order.isEmpty());
        assertEquals(new BigDecimal("0.00"), order.calculateTotal());
    }

    @Test
    void decimalTotalsAreExactAndRoundedToCents() {
        Order order = new Order(36, customer);
        order.addItem(new Product(4, "Washer", new BigDecimal("0.10")), 3);
        order.addItem(new Product(5, "Clip", new BigDecimal("0.005")), 1);

        // 0.30 + 0.005 = 0.305, rounded half-up once at the total
        assertEquals(new BigDecimal("0.31"), order.calculateTotal());
    }

    @Test
    void addItemRejectsInvalidQuantity() {
        Order order = new Order(35, customer);

        assertThrows(IllegalArgumentException.class, () -> order.addItem(hammer, 0));
    }

    @Test
    void getItemsReturnsUnmodifiableList() {
        Order order = new Order(35, customer);
        order.addItem(hammer, 1);

        List<OrderItem> items = order.getItems();

        assertThrows(UnsupportedOperationException.class, () -> items.add(new OrderItem(hammer, 1)));
    }

    @Test
    void newOrderIsOpenAndPaidOrderIsLocked() {
        Order order = new Order(35, customer);
        order.addItem(hammer, 1);
        assertEquals(OrderStatus.OPEN, order.getStatus());

        order.markPaid();

        assertEquals(OrderStatus.PAID, order.getStatus());
        assertThrows(IllegalStateException.class, () -> order.addItem(hammer, 1));
        assertThrows(IllegalStateException.class, order::markPaid);
    }

    @Test
    void emptyOrderCannotBeMarkedPaid() {
        Order order = new Order(35, customer);

        assertThrows(IllegalStateException.class, order::markPaid);
    }

    @Test
    void orderRequiresACustomer() {
        assertThrows(IllegalArgumentException.class, () -> new Order(35, null));
    }

    @Test
    void printSummaryShowsInvoiceDetails() {
        Order order = new Order(35, customer);
        order.addItem(hammer, 1);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            new com.paynestsystem.service.OrderSummary().printSummary(order);
        } finally {
            System.setOut(originalOut);
        }

        String summary = output.toString();
        assertTrue(summary.contains("PAYMENT INVOICE"));
        assertTrue(summary.contains("Hammer"));
        assertTrue(summary.contains("Total Amount:"));
        assertTrue(summary.contains("150,00"));
        assertTrue(summary.contains("Status: OPEN"));
    }
}

