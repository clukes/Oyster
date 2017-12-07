package com.tfl.billing.helpers;

import com.oyster.ScanListener;
import com.tfl.billing.*;
import com.tfl.billing.adapters.ICustomerDatabase;
import com.tfl.billing.adapters.IOysterCardReader;
import com.tfl.external.Customer;

import java.util.*;

public class JourneyHelper implements ScanListener {
    private final List<JourneyEvent> eventLog = new ArrayList<JourneyEvent>();
    private final Set<UUID> currentlyTravelling = new HashSet<UUID>();

    private final ICustomerDatabase customerDatabase;

    public JourneyHelper(ICustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
    }

    public List<Journey> getCustomerJourneys(Customer customer)
    {
        List<JourneyEvent> customerJourneyEvents = getCustomerJourneyEvents(customer);
        List<Journey> journeys = getJourneys(customerJourneyEvents);
        return journeys;
    }

    private List<Journey> getJourneys(List<JourneyEvent> customerJourneyEvents) {
        List<Journey> journeys = new ArrayList<Journey>();

        JourneyEvent start = null;
        for (JourneyEvent event : customerJourneyEvents) {
            if (event instanceof JourneyStart) {
                start = event;
            }
            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            }
        }
        return journeys;
    }

    private List<JourneyEvent> getCustomerJourneyEvents(Customer customer) {
        List<JourneyEvent> customerJourneyEvents = new ArrayList<JourneyEvent>();
        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }
        return customerJourneyEvents;
    }

    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEnd(cardId, readerId));
            currentlyTravelling.remove(cardId);
        } else {
            if (customerDatabase.isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyStart(cardId, readerId));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    public void connect(IOysterCardReader cardReader) {
        cardReader.register(this);
    }
}
