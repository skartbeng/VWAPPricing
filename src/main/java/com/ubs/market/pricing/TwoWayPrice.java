package com.ubs.market.pricing;

/**
 * Created by karthi on 19/03/2017.
 */
public interface TwoWayPrice {
    Instrument getInstrument();
    State getState();
    double getBidPrice();
    double getOfferAmount();
    double getOfferPrice();
    double getBidAmount();
}
