package com.tfl.billing.helpers;

import com.tfl.billing.Journey;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.math.BigDecimal;
import java.util.List;

public class PriceHelper {
    public final BigDecimal PEAK_LONG = new BigDecimal(3.80);
    public final BigDecimal PEAK_SHORT = new BigDecimal(2.90);
    public final BigDecimal OFF_PEAK_LONG = new BigDecimal(2.70);
    public final BigDecimal OFF_PEAK_SHORT = new BigDecimal(1.60);

    public final BigDecimal OFF_PEAK_CAP = new BigDecimal(7.00);
    public final BigDecimal PEAK_CAP = new BigDecimal(9.00);

    private static PriceHelper instance = new PriceHelper();

    private PriceHelper() { }

    public static PriceHelper getInstance() {
        return instance;
    }

    public BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private boolean peakJourney(Journey journey) {
        return peakJourney(journey.startTime()) || peakJourney(journey.endTime());
    }

    private boolean peakJourney(DateTime time) {
        int hour = time.getHourOfDay();
        return (hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 19);
    }

    private boolean longJourney(Journey journey) {
        return longJourney(journey.startTime(), journey.endTime());
    }

    private boolean longJourney(DateTime startTime, DateTime endTime) {
        int length = Minutes.minutesBetween(startTime, endTime).getMinutes();
        return (length >= 25);
    }

    public BigDecimal calculateJourneyPrice(Journey journey) {
        if(peakJourney(journey)) {return calculatePeakJourneyPrice(journey);}
        return calculateOffPeakJourneyPrice(journey);
    }

    private BigDecimal calculatePeakJourneyPrice(Journey journey) {
        if(longJourney(journey)) {return PEAK_LONG;}
        return PEAK_SHORT;
    }

    private BigDecimal calculateOffPeakJourneyPrice(Journey journey) {
        if(longJourney(journey)) {return OFF_PEAK_LONG;}
        return OFF_PEAK_SHORT;
    }

    public BigDecimal calculateTotalPrice(List<Journey> journeys) {
        BigDecimal total = new BigDecimal(0);
        boolean anyPeak = false;
        for (Journey journey : journeys) {
            if (!anyPeak && peakJourney(journey)) { anyPeak = true; }
            total = total.add(calculateJourneyPrice(journey));
        }
        if(anyPeak && total.compareTo(PEAK_CAP) > 0) {return PEAK_CAP;}
        if(total.compareTo(OFF_PEAK_CAP) > 0) {return OFF_PEAK_CAP;}
        return roundToNearestPenny(total);
    }
}
