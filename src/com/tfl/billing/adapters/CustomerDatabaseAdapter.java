package com.tfl.billing.adapters;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;

import java.util.List;
import java.util.UUID;

public class CustomerDatabaseAdapter implements ICustomerDatabase {
    private final CustomerDatabase database;

    public CustomerDatabaseAdapter() {
        this.database = CustomerDatabase.getInstance();
    }

    @Override
    public List<Customer> getCustomers() {
        return database.getCustomers();
    }

    @Override
    public boolean isRegisteredId(UUID cardId) {
        return database.isRegisteredId(cardId);
    }
}
