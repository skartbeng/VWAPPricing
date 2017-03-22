package com.ubs.market.pricing;

/**
 * Created by karthi on 19/03/2017.
 Use Immutable object. Separate object to represent the VWAP price*
 */
public class VWAPTwoWayPrice implements TwoWayPrice {
    private final Instrument instrument;
    private final double bidPrice;
    private final double offerPrice;

    public VWAPTwoWayPrice(Instrument instrument, double bidPrice, double offerPrice) {
        this.instrument = instrument;
        this.bidPrice = bidPrice;
        this.offerPrice = offerPrice;
    }

    @Override
    public Instrument getInstrument() {
        return instrument;
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public double getBidPrice() {
        return bidPrice;
    }

    @Override
    public double getOfferAmount() {
        return 0;
    }

    @Override
    public double getOfferPrice() {
        return offerPrice;
    }

    @Override
    public double getBidAmount() {
        return 0;
    }
}
