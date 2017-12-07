package com.tfl.billing.adapters;

import com.oyster.OysterCard;
import com.oyster.ScanListener;

public interface IOysterCardReader {
    void register(ScanListener scanListener);

    void touch(OysterCard myCard);
}
