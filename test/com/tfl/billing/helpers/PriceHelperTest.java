package com.tfl.billing.helpers;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.Journey;
import com.tfl.billing.JourneyEnd;
import com.tfl.billing.JourneyStart;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class PriceHelperTest {
    PriceHelper calculator = PriceHelper.getInstance();

    OysterCardReader reader = new OysterCardReader();
    OysterCard card = new OysterCard();

    @Test
    public void peakShortJourneyGivesCorrectPrice() {
        DateTime startTime = new DateTime().withHourOfDay(6).withMinuteOfHour(0);
        DateTime endTime = new DateTime().withHourOfDay(6).withMinuteOfHour(24);
        Journey journey = createTestJourney(startTime, endTime);

        assertTrue(calculator.calculateJourneyPrice(journey).equals(calculator.PEAK_SHORT));
    }

    @Test
    public void peakLongJourneyGivesCorrectPrice() {
        DateTime startTime = new DateTime().withHourOfDay(17).withMinuteOfHour(0);
        DateTime endTime = new DateTime().withHourOfDay(17).withMinuteOfHour(25);
        Journey journey = createTestJourney(startTime, endTime);

        assertTrue(calculator.calculateJourneyPrice(journey).equals(calculator.PEAK_LONG));
    }

    @Test
    public void offPeakShortJourneyGivesCorrectPrice() {
        DateTime startTime = new DateTime().withHourOfDay(10).withMinuteOfHour(0);
        DateTime endTime = new DateTime().withHourOfDay(10).withMinuteOfHour(5);
        Journey journey = createTestJourney(startTime, endTime);

        assertTrue(calculator.calculateJourneyPrice(journey).equals(calculator.OFF_PEAK_SHORT));
    }

    @Test
    public void offPeakLongJourneyGivesCorrectPrice() {
        DateTime startTime = new DateTime().withHourOfDay(13).withMinuteOfHour(0);
        DateTime endTime = new DateTime().withHourOfDay(14).withMinuteOfHour(0);
        Journey journey = createTestJourney(startTime, endTime);

        assertTrue(calculator.calculateJourneyPrice(journey).equals(calculator.OFF_PEAK_LONG));
    }

    @Test
    public void offPeakJourneysAreCapped() {
        int noOfJourneys = (calculator.OFF_PEAK_CAP.divide(calculator.OFF_PEAK_LONG, RoundingMode.UP)).intValue();
        ArrayList<Journey> journeys = new ArrayList<Journey>();
        for(int i = 0; i <= noOfJourneys; i++) {
            DateTime startTime = new DateTime().withHourOfDay(15).withMinuteOfHour(0);
            DateTime endTime = new DateTime().withHourOfDay(15).withMinuteOfHour(30);
            journeys.add(createTestJourney(startTime, endTime));
        }
        assertTrue(calculator.calculateTotalPrice(journeys).equals(calculator.OFF_PEAK_CAP));
    }

    @Test
    public void peakJourneysAreCapped() {
        int noOfJourneys = (calculator.PEAK_CAP.divide(calculator.PEAK_LONG, RoundingMode.UP)).intValue();
        ArrayList<Journey> journeys = new ArrayList<Journey>();
        for(int i = 0; i <= noOfJourneys; i++) {
            DateTime startTime = new DateTime().withHourOfDay(6).withMinuteOfHour(0);
            DateTime endTime = new DateTime().withHourOfDay(7).withMinuteOfHour(0);
            journeys.add(createTestJourney(startTime, endTime));
        }
        assertTrue(calculator.calculateTotalPrice(journeys).equals(calculator.PEAK_CAP));
    }

    private void setCurrentTime(DateTime time) {
        DateTimeUtils.setCurrentMillisFixed(time.getMillis());
    }

    private Journey createTestJourney(DateTime startTime, DateTime endTime)
    {
        setCurrentTime(startTime);
        JourneyStart start = new JourneyStart(card.id(), reader.id());
        setCurrentTime(endTime);
        JourneyEnd end = new JourneyEnd(card.id(), reader.id());
        return new Journey(start, end);
    }
}
