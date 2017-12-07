package com.tfl.billing.adapters;

import com.tfl.external.Customer;

import java.util.List;
import java.util.UUID;

public interface ICustomerDatabase {
    List<Customer> getCustomers();

    boolean isRegisteredId(UUID cardId);
}
