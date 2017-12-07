package com.tfl.billing.adapters;

import com.tfl.billing.Journey;
import com.tfl.external.Customer;
import com.tfl.external.PaymentsSystem;

import java.math.BigDecimal;
import java.util.List;

public class PaymentsSystemAdapter implements IPaymentsSystem {
    private final PaymentsSystem payments;

    public PaymentsSystemAdapter() {
        this.payments = PaymentsSystem.getInstance();
    }

    @Override
    public void charge(Customer customer, List<Journey> journeys, BigDecimal bigDecimal) {
        payments.charge(customer, journeys, bigDecimal);
    }
}
