package com.ubs.market.pricing;

/**
 * Created by karthi on 19/03/2017.
 */
public class MarketDataValidationFailedException extends RuntimeException {
    public MarketDataValidationFailedException(String errorMsg) {
        super(errorMsg);
    }

    public MarketDataValidationFailedException(String errorMsg,Exception err) {
        super(errorMsg,err);
    }
}
