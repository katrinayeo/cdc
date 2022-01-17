package com.example.cdc;

import com.example.cdc.Model.Candlestick;
import com.example.cdc.Model.CheckCandle;
import com.example.cdc.Model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataProcessing {

    @Autowired
    private APIController apiController;

    @EventListener(ApplicationReadyEvent.class)
    public void call() {
        System.out.println("running");
        verifyAllCandleStick();
    }

    private final String timeframe = "5m";
    private final String instrument_name = "BTC_USDT";
    private final Map<String, Long> map = new HashMap<String, Long>() {{
        put("1m", 60000L);
        put("5m", 300000L);
        put("15m", 900000L);
        put("30m", 1800000L);
        put("14D", 1209600000L);
    }};


    //list for trade and candlestick is time sorted
    public void verifyAllCandleStick() {
        System.out.println("Time interval used is " + timeframe);
        System.out.println("Trading pair used is " + instrument_name);

        List<Trade> tradeList = apiController.getTrades(instrument_name);
        /*List<Trade> tradeList = new ArrayList<>(); //p q s d t dataTime i
        tradeList.add(new Trade(40000.00, 0.000046, "SELL", 2161703746513036512L, 1642090091961L, 1642090091961L, "BTC_USDT"));
        tradeList.add(new Trade(41000.07, 0.000046, "SELL", 2161703746513036512L, 1642090691861L, 1642090591861L, "BTC_USDT"));
        tradeList.add(new Trade(42000.07, 0.000046, "SELL", 2161703746513036512L, 1642090900861L, 1642260591861L, "BTC_USDT"));
        tradeList.add(new Trade(43000.07, 0.000046, "SELL", 2161703746513036512L, 1642091091861L, 1642260591861L, "BTC_USDT"));
        tradeList.add(new Trade(44000.07, 0.000046, "SELL", 2161703746513036512L, 1642091200000L, 1642260591861L, "BTC_USDT"));
        tradeList.add(new Trade(45000.07, 0.000046, "SELL", 2161703746513036512L, 1642092200000L, 1642260591861L, "BTC_USDT"));*/
        List<Candlestick> candlestickList = apiController.getCandleSticks(timeframe, instrument_name);
        /*List<Candlestick> candlestickList = new ArrayList<>();
        candlestickList.add(new Candlestick(1642090800000L, 41000.07, 41000.07, 41000.07, 41000.07, 255.31047));
        candlestickList.add(new Candlestick(1642091100000L, 42000.07, 43000.07, 42000.07, 43000.07, 255.31047));
        candlestickList.add(new Candlestick(1642091400000L, 30000.07, 30000.07, 30000.07, 30000.07, 30000.07));*/

        for (Iterator<Candlestick> citerator = candlestickList.iterator(); citerator.hasNext(); ) {
            Candlestick c = citerator.next();
            long[] candleRange = getTimeRange(c.getT(), timeframe);

            CheckCandle checkCandle = new CheckCandle();
            for (Iterator<Trade> titerator = tradeList.iterator(); titerator.hasNext(); ) {
                Trade t = titerator.next();
                if (candleRange[0] > t.getT()) {
                    System.out.println("Earliest trade with timestamp " + t.getT() + " is before candle with timestamp " + candleRange[0] + " moving on to next trade...");
                    titerator.remove();
                } else if (Math.max(candleRange[0], t.getT()) == Math.min(t.getT(), candleRange[1])) { //building candle
                    buildCandle(checkCandle, t);
                    titerator.remove();
                    if (!titerator.hasNext()) {
                        printResult(verifyCandle(checkCandle, c), c, checkCandle);
                    }
                } else if (t.getT() > candleRange[1]) { //finished building candle
                    if (checkCandle.getCount() == 0) {
                        System.out.println("Earliest trade with timestamp " + t.getT() + " is after candle with timestamp " + candleRange[1]);
                    } else {
                        printResult(verifyCandle(checkCandle, c), c, checkCandle);
                    }
                    break;
                }
            }
        }

    }

    public void printResult(boolean accurate, Candlestick c, CheckCandle checkCandle) {
        if (accurate) {
            System.out.println("Candle with timestamp " + c.getT() + " is accurate. It had " + checkCandle.getCount() + " trade(s) carried out");
        } else {
            System.out.println("Candle with timestamp " + c.getT() + " is inaccurate");
        }
    }

    public void buildCandle(CheckCandle checkCandle, Trade t) {
        double tradePrice = t.getP();
        if (checkCandle.getCount() == 0) { //First Trade of Candle
            checkCandle.setO(tradePrice);
            checkCandle.setL(tradePrice);
            checkCandle.setH(tradePrice);
        }
        if (tradePrice > checkCandle.getH()) {
            checkCandle.setH(tradePrice); //Higher High
        } else if (tradePrice < checkCandle.getL()) {
            checkCandle.setL(tradePrice); //Lower Low
        }
        checkCandle.setC(tradePrice); //always update close
        checkCandle.setCount(checkCandle.getCount() + 1);
    }

    //Verify accuracy of Candlestick after building info with trades
    public boolean verifyCandle(CheckCandle cc, Candlestick c) {
        return (cc.getL() == c.getL() && cc.getH() == c.getH() && cc.getO() == c.getO() && cc.getC() == c.getC());
    }

    //Returns range of time given end of candlestick
    public long[] getTimeRange(long endTime, String timeframe) {
        return new long[]{endTime - map.get(timeframe), endTime};
    }
}
