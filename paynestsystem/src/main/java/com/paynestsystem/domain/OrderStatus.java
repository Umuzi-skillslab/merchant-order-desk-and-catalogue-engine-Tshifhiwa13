package com.paynestsystem.domain;

/**
 * Lifecycle of an order.
 * <ul>
 *   <li>{@code OPEN}: items can be added; the order has not been paid.</li>
 *   <li>{@code PAID}: checkout succeeded; the order is locked.</li>
 * </ul>
 */
public enum OrderStatus {
    OPEN,
    PAID
}

