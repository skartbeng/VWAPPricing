package com.ubs.market.pricing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by karthi on 19/03/2017.
 */
public class CalculatorTest {
    private Calculator singleUpdateVwaPClaculator = null;
    Calculator multipleUpdateVwaPClaculator = null;
    MarketUpdate marketUpdate4 = null;
    MarketUpdate marketUpdate01 = null;

    @Before
    public void setup(){
        singleUpdateVwaPClaculator = new VWAPCalculator();
        multipleUpdateVwaPClaculator = new VWAPCalculator();

        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,105.00, 12, 106.00,16);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate0);

        TwoWayPrice mktPriceUpdate1 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,107.00, 15, 108.00,20);
        MarketUpdate marketUpdate1 = new MarketUpdateImpl(Market.MARKET1,mktPriceUpdate1);

        TwoWayPrice mktPriceUpdate2 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,108.00, 20, 108.50,15);
        MarketUpdate marketUpdate2 = new MarketUpdateImpl(Market.MARKET2,mktPriceUpdate2);

        TwoWayPrice mktPriceUpdate3 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,109.00, 25, 109.25,30);
        MarketUpdate marketUpdate3 = new MarketUpdateImpl(Market.MARKET3,mktPriceUpdate3);

        TwoWayPrice mktPriceUpdate4 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,106.00, 38, 107.00,25);
        marketUpdate4 = new MarketUpdateImpl(Market.MARKET4,mktPriceUpdate4);

        TwoWayPrice mktPriceUpdate01 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,106.00, 15 , 107.10,10);
        marketUpdate01 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate01);

        singleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate0);
        singleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate1);
        singleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate2);
        singleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate3);

        multipleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate0);
        multipleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate1);
        multipleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate2);
        multipleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void canHandleNullData(){
        Calculator calculator = new VWAPCalculator();
        calculator.applyMarketUpdate(null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void canHandleWhenMarketIsNull(){
        Calculator calculator = new VWAPCalculator();
        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,105.00, 12, 106.00,16);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(null,mktPriceUpdate0);
        calculator.applyMarketUpdate(marketUpdate0);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void canHandleWhenInstrumentIsNull(){
        Calculator calculator = new VWAPCalculator();
        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(null,State.FIRM,105.00, 12, 106.00,16);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate0);
        calculator.applyMarketUpdate(marketUpdate0);
        fail();
    }

    @Test(expected = MarketDataValidationFailedException.class)
    public void canHandleWhenZeroBidPrice(){
        Calculator calculator = new VWAPCalculator();
        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,0.00, 12, 106.00,16);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate0);
        calculator.applyMarketUpdate(marketUpdate0);
        fail();
    }

    @Test(expected = MarketDataValidationFailedException.class)
    public void canHandleWhenNegativeBidAmount(){
        Calculator calculator = new VWAPCalculator();
        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,105.00, -0.01, 106.00,16);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate0);
        calculator.applyMarketUpdate(marketUpdate0);
        fail();
    }

    @Test(expected = MarketDataValidationFailedException.class)
    public void canHandleWhenZeroOfferPrice(){
        Calculator calculator = new VWAPCalculator();
        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,105.00, 12, -102.00,16);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate0);
        calculator.applyMarketUpdate(marketUpdate0);
        fail();
    }

    @Test(expected = MarketDataValidationFailedException.class)
    public void canHandleWhenNegativeOfferAmount(){
        Calculator calculator = new VWAPCalculator();
        TwoWayPrice mktPriceUpdate0 = new MarketTwoWayPrice(Instrument.INSTRUMENT0,State.FIRM,105.00, -0.01, 106.00,0.00);
        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0,mktPriceUpdate0);
        calculator.applyMarketUpdate(marketUpdate0);
        fail();
    }

    @Test
    public void singleUpdate(){
        TwoWayPrice twoWayPriceUpdate4 = singleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate4);
        Assert.assertTrue(Instrument.INSTRUMENT0.equals(twoWayPriceUpdate4.getInstrument()));
        Assert.assertEquals(107.0727,twoWayPriceUpdate4.getBidPrice(),0.01);
        Assert.assertEquals(107.8868,twoWayPriceUpdate4.getOfferPrice(),0.01);
    }

    @Test
    public void multipleUpdate(){
        TwoWayPrice twoWayPriceUpdate4 = multipleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate4);
        Assert.assertTrue(Instrument.INSTRUMENT0.equals(twoWayPriceUpdate4.getInstrument()));
        Assert.assertEquals(107.0727,twoWayPriceUpdate4.getBidPrice(),0.01);
        Assert.assertEquals(107.8868,twoWayPriceUpdate4.getOfferPrice(),0.01);

        TwoWayPrice twoWayPriceUpdate01 = multipleUpdateVwaPClaculator.applyMarketUpdate(marketUpdate01);
        Assert.assertTrue(Instrument.INSTRUMENT0.equals(twoWayPriceUpdate01.getInstrument()));
        Assert.assertEquals(107.1504,twoWayPriceUpdate01.getBidPrice(),0.01);
        Assert.assertEquals(108.11,twoWayPriceUpdate01.getOfferPrice(),0.01);
    }
}
