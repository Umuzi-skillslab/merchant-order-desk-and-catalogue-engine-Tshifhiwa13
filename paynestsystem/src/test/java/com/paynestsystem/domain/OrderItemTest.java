package com.paynestsystem.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class OrderItemTest {

    @Test
    void testCalculateTotal() {
        Product nails = new Product(1, "Box of Nails", BigDecimal.valueOf(120));
        OrderItem item = new OrderItem(nails, 3);

        // Use compareTo to ignore scale differences (360 vs 360.0)
        assertEquals(0, item.calculateTotal().compareTo(BigDecimal.valueOf(360)),
            "3 Boxes of Nails at R120 each should total R360.00");
    }

    @Test
    void testInvalidQuantityThrowsException() {
        Product hammer = new Product(1, "Hammer", BigDecimal.valueOf(150));

        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem(hammer, 0),
            "Quantity of 0 should throw exception");

        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem(hammer, -5),
            "Negative quantity should throw exception");
    }

    @Test
    void testNullProductThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderItem(null, 2),
            "Null product should throw exception");
    }
}