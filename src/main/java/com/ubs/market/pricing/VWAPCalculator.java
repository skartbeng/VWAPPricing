package com.ubs.market.pricing;

import java.util.EnumMap;

/**
 * Created by karthi on 19/03/2017.
 */
public class VWAPCalculator implements Calculator {
    //EnumMap is not threadSafe, however as per the instruction this class will be used from single thread. so It would be safe to use enummap
    EnumMap<Instrument,EnumMap<Market,TwoWayPrice>> marketData = new EnumMap<Instrument,EnumMap<Market,TwoWayPrice>>(Instrument.class);
    private volatile boolean initialised = false;
    public VWAPCalculator(){
        init();
    }

    public void init(){
        for (Instrument instrument :Instrument.values()) {
            EnumMap<Market, TwoWayPrice> priceEnumMap = new EnumMap<>(Market.class);
            marketData.put(instrument, priceEnumMap);
        }

    }
    @Override
    public TwoWayPrice applyMarketUpdate(final MarketUpdate twoWayMarketPrice) {

        validate(twoWayMarketPrice);
        //insert the new update to the collection
        Instrument instrument = twoWayMarketPrice.getTwoWayPrice().getInstrument();
        EnumMap<Market, TwoWayPrice> twoWayPriceEnumMap = marketData.get(instrument);
        if(twoWayPriceEnumMap!=null){
            Market market = twoWayMarketPrice.getMarket();
            //There is no need to check for existing market, the collection will always maintains the last price update per market.
            //if no entry exists, it will create otherwise replace the existing mapping.
            //prevent multiple map look up and improve efficiency
            twoWayPriceEnumMap.put(market,twoWayMarketPrice.getTwoWayPrice());
        }
        else {//instrument does not exist in the map
            twoWayPriceEnumMap = new EnumMap<>(Market.class);
            twoWayPriceEnumMap.put(twoWayMarketPrice.getMarket(),twoWayMarketPrice.getTwoWayPrice());
            marketData.put(instrument, twoWayPriceEnumMap);
        }

        double sigmaBidPriceAmount=0.00;
        double sigmaBidAmount=0.00;
        double sigmaOfferPriceAmount=0.00;
        double sigmaOfferAmount=0.00;


        //As there is lack of details on the use of State, it is being ignored in the calculation
        //Also no calculation for amount/volume when calculating VWAP pricing
        for (TwoWayPrice twoWayPrice :twoWayPriceEnumMap.values()) {
            if(twoWayPrice.getBidAmount()>0.0 && twoWayPrice.getBidPrice()>0.0){
                sigmaBidPriceAmount+=(twoWayPrice.getBidAmount()*twoWayPrice.getBidPrice());
                sigmaBidAmount+=twoWayPrice.getBidAmount();
            }
            if(twoWayPrice.getOfferPrice()>0.00 && twoWayPrice.getOfferAmount()>0.00){
                sigmaOfferPriceAmount+=(twoWayPrice.getOfferAmount()*twoWayPrice.getOfferPrice());
                sigmaOfferAmount+=twoWayPrice.getOfferAmount();
            }
        }
        TwoWayPrice vwapPrice = new VWAPTwoWayPrice(instrument,sigmaBidPriceAmount/sigmaBidAmount,sigmaOfferPriceAmount/sigmaOfferAmount);
        return vwapPrice;
    }

    /**
     * Validate the market Data update to make sure
     *  1. Market is not null
     *  2. Instrument is not null
     *  3. price and volume are positive
     *  4. Optionally check State if required
     * @param twoWayMarketPrice
     * @throws MarketDataValidationFailedException unchecked exception
     */
    private void validate(MarketUpdate twoWayMarketPrice) {

        //In production more granular level validation is required
        if(twoWayMarketPrice==null || twoWayMarketPrice.getMarket()==null ||twoWayMarketPrice.getTwoWayPrice()==null ||twoWayMarketPrice.getTwoWayPrice().getInstrument()==null){
            throw new IllegalArgumentException("Market Data update is null or market, instrument, price objects are null");
        }

        if(twoWayMarketPrice.getTwoWayPrice().getBidPrice()<=0.00 || twoWayMarketPrice.getTwoWayPrice().getBidAmount()<=0.00 || twoWayMarketPrice.getTwoWayPrice().getOfferPrice()<=0.00 || twoWayMarketPrice.getTwoWayPrice().getOfferAmount()<=0.00){
            throw new MarketDataValidationFailedException("MarketDataUpdate cannot contains zero or negative values  price or  volume");
        }

    }
}
