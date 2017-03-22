# VWAPPricing
Satkuru Karthigeyan
1. Requires JDK8
2. Using EnumMap for the market data collection, which is not thread safe. However, the Calculator.applyMarketUpdate is invoked from single thread. Hence it is safe.
3. Best to run from IntelliJ
4. State Enum is ignored in the price calculation, no details in the instruction
5. Collection always maintains the latest update per market per instrument,
6. VWAP two way price does not calculation the bid and offer amount as it not explained in the instruction
7. Have Junit tests
8. Using immutable objects where possible
9. Validating the marketDataUpdate using unchecked exception (runtimeexception) to avoid breaking the Calculator's method
