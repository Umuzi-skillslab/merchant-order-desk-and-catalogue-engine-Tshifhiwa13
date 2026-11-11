package com.paynestsystem.service;

import com.paynestsystem.payment.PaymentMethod;

class RecordingPayment extends PaymentMethod {

    private final boolean approve;
    private double chargedAmount = -1;
    private int calls;

    RecordingPayment(boolean approve) {
        this.approve = approve;
    }

    @Override
    protected boolean doProcess(double amount) {
        chargedAmount = amount;
        calls++;
        return approve;
    }

    @Override
    public String getPaymentType() {
        return "TEST";
    }

    double getChargedAmount() {
        return chargedAmount;
    }

    int getCalls() {
        return calls;
    }
}

