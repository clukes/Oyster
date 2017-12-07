package com.tfl.billing;

import com.oyster.OysterCard;
import com.tfl.billing.adapters.ICustomerDatabase;
import com.tfl.billing.adapters.IPaymentsSystem;
import com.tfl.external.Customer;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TravelTrackerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    ICustomerDatabase database = context.mock(ICustomerDatabase.class);
    IPaymentsSystem payments = context.mock(IPaymentsSystem.class);

    OysterCard card = new OysterCard();
    List<Customer> customer = new ArrayList<Customer>() {{
        this.add(new Customer("John Smith", card));
        this.add(new Customer("Joe Bloggs", new OysterCard()));
    }};

    TravelTracker tracker = new TravelTracker(database, payments);

    @Test
    public void accountsAreCharged() {
        context.checking(new Expectations() {{
            exactly(1).of(database).getCustomers(); will(returnValue(customer));
            exactly(1).of(payments).charge(with(customer.get(0)), with(aNonNull(List.class)), with(aNonNull(BigDecimal.class)));
            exactly(1).of(payments).charge(with(customer.get(1)), with(aNonNull(List.class)), with(aNonNull(BigDecimal.class)));
        }});

        tracker.chargeAccounts();
    }
}
