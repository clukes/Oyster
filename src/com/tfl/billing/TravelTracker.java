package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.tfl.billing.adapters.*;
import com.tfl.billing.helpers.JourneyHelper;
import com.tfl.billing.helpers.PriceHelper;
import com.tfl.external.Customer;

import java.math.BigDecimal;
import java.util.List;

public class TravelTracker {
    private final ICustomerDatabase customerDatabase;
    private final IPaymentsSystem paymentsSystem;
    private final JourneyHelper journeyHelper;
    private final PriceHelper priceHelper = PriceHelper.getInstance();

    public TravelTracker()
    {
        this.customerDatabase = new CustomerDatabaseAdapter();
        this.paymentsSystem = new PaymentsSystemAdapter();
        this.journeyHelper = new JourneyHelper(customerDatabase);
    }

    public TravelTracker(ICustomerDatabase customerDatabase, IPaymentsSystem paymentsSystem) {
        this.customerDatabase = customerDatabase;
        this.paymentsSystem = paymentsSystem;
        this.journeyHelper = new JourneyHelper(customerDatabase);
    }

    public void chargeAccounts() {
        List<Customer> customers = customerDatabase.getCustomers();
        for (Customer customer : customers) {
            totalJourneysFor(customer);
        }
    }

    private void totalJourneysFor(Customer customer) {
        List<Journey> journeys = journeyHelper.getCustomerJourneys(customer);

        BigDecimal customerTotal = priceHelper.calculateTotalPrice(journeys);

        paymentsSystem.charge(customer, journeys, customerTotal);
    }

    public void connect(OysterCardReader... cardReaders) {
        for(OysterCardReader cardReader : cardReaders) {
            journeyHelper.connect(new OysterCardReaderAdapter(cardReader));
        }
    }

    public void connect(IOysterCardReader... cardReaders) {
        for (IOysterCardReader cardReader : cardReaders) {
            journeyHelper.connect(cardReader);
        }
    }
}
