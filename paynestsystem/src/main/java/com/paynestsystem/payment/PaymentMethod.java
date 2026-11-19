package com.paynestsystem.payment;

public interface PaymentMethod {

    boolean processPayment(double amount);

    String getPaymentType();
}
