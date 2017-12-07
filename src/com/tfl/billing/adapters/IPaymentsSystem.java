package com.tfl.billing.adapters;

import com.tfl.billing.Journey;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentsSystem {
    void charge(Customer customer, List<Journey> journeys, BigDecimal bigDecimal);
}
