package com.tfl.billing.adapters;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.oyster.ScanListener;

public class OysterCardReaderAdapter implements IOysterCardReader {
    private final OysterCardReader reader;

    public OysterCardReaderAdapter(OysterCardReader reader) {
        this.reader = reader;
    }

    @Override
    public void register(ScanListener scanListener) {
        reader.register(scanListener);
    }

    @Override
    public void touch(OysterCard card) {
        reader.touch(card);
    }
}
