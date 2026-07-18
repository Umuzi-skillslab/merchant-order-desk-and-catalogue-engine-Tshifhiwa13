package com.paynestsystem.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class OrderItemTest {

    @Test
    public void testHammerSubtotal() {
        Product hammer = new Product(1, "Hammer", 149.90);
        OrderItem item = new OrderItem(hammer, 1);

        assertEquals("Hammer subtotal should equal 149.90", 149.90, item.calculateTotal(), 0.001);
    }

    @Test
    public void testDrillSubtotal() {
        Product drill = new Product(2, "Electric Drill", 1999.99);
        OrderItem item = new OrderItem(drill, 1);

        assertEquals("Drill subtotal should equal 1999.99", 1999.99, item.calculateTotal(), 0.001);
    }

    @Test
    public void testNailsSubtotal() {
        Product nails = new Product(3, "Box of Nails", 120.05);
        OrderItem item = new OrderItem(nails, 2);

        double expectedTotal = 240.10; // 120.05 * 2
        assertEquals("Nails subtotal should equal 240.10", expectedTotal, item.calculateTotal(), 0.001);
    }

    @Test
    public void testGettersReturnCorrectValues() {
        Product drill = new Product(2, "Electric Drill", 1999.99);
        OrderItem item = new OrderItem(drill, 1);

        assertEquals("Product getter should return the drill object", drill, item.getProduct());
        assertEquals("Quantity getter should return 1", 1, item.getQuantity());
    }
}
