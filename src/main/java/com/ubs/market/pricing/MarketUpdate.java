package com.ubs.market.pricing;

/**
 * Created by karthi on 19/03/2017.
 */
public interface MarketUpdate {
    Market getMarket();
    TwoWayPrice getTwoWayPrice();
}
