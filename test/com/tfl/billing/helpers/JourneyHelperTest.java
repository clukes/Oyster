package com.tfl.billing.helpers;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.UnknownOysterCardException;
import com.tfl.billing.adapters.ICustomerDatabase;
import com.tfl.billing.adapters.IOysterCardReader;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class JourneyHelperTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    ICustomerDatabase database = context.mock(ICustomerDatabase.class);
    IOysterCardReader reader1 = context.mock(IOysterCardReader.class, "Reader 1");
    IOysterCardReader reader2 = context.mock(IOysterCardReader.class, "Reader 2");

    OysterCardReader reader = new OysterCardReader();
    OysterCard card = new OysterCard();

    JourneyHelper journeyHelper = new JourneyHelper(database);

    @Test
    public void connectRegistersCorrectNumberOfTimes() {
        context.checking(new Expectations() {{
            exactly(1).of(reader1).register(journeyHelper);
            exactly(1).of(reader2).register(journeyHelper);
        }});

        journeyHelper.connect(reader1);
        journeyHelper.connect(reader2);
    }

    @Test (expected = UnknownOysterCardException.class)
    public void unregisteredCardScannedGivesError() {
        context.checking(new Expectations() {{
            exactly(1).of(database).isRegisteredId(card.id()); will(returnValue(false));
        }});

        journeyHelper.cardScanned(card.id(), reader.id());
    }

    @Test
    public void registeredCardScannedIsSuccessful() {
        context.checking(new Expectations() {{
            exactly(1).of(database).isRegisteredId(card.id()); will(returnValue(true));
        }});

        journeyHelper.cardScanned(card.id(), reader.id());
        journeyHelper.cardScanned(card.id(), reader.id());
    }
}