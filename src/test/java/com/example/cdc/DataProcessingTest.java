package com.example.cdc;

import com.example.cdc.Model.Candlestick;
import com.example.cdc.Model.CheckCandle;
import com.example.cdc.Model.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataProcessingTest {

    @Spy
    @InjectMocks
    DataProcessing dataProcessing;

    @Mock
    APIController apiController;

    @Mock
    CheckCandle cc;

    @Mock
    Candlestick c;

    private List<Trade> tradeList = new ArrayList<>();
    private List<Candlestick> candlestickList = new ArrayList<>();

    //This test suite is written with 5m candlestick for BTC_USDT
    @Test
    public void test1Trade() {
        tradeList.add(new Trade(41000.07, 0.000046, "SELL", 2161703746513036512L, 1642090691861L, 1642090591861L, "BTC_USDT"));
        c = new Candlestick(1642090800000L, 41000.07, 41000.07, 41000.07, 41000.07, 255.31047);
        candlestickList.add(c);
        cc = new CheckCandle(41000.07, 41000.07, 41000.07, 41000.07, 0);

        when(apiController.getTrades(any())).thenReturn(tradeList);
        when(apiController.getCandleSticks(any(), any())).thenReturn(candlestickList);

        dataProcessing.verifyAllCandleStick();
        verify(dataProcessing, times(1)).buildCandle(any(), any());
        verify(dataProcessing, times(1)).verifyCandle(any(), any());
        assertTrue(dataProcessing.verifyCandle(cc, c));
    }

    @Test
    public void test2Trades() {
        tradeList.add(new Trade(42000.07, 0.000046, "SELL", 2161703746513036512L, 1642090900861L, 1642260591861L, "BTC_USDT"));
        tradeList.add(new Trade(43000.07, 0.000046, "SELL", 2161703746513036512L, 1642091091861L, 1642260591861L, "BTC_USDT"));
        c = new Candlestick(1642091100000L, 42000.07, 43000.07, 42000.07, 43000.07, 255.31047);
        candlestickList.add(c);
        cc = new CheckCandle(42000.07, 43000.07, 42000.07, 43000.07, 0);

        when(apiController.getTrades(any())).thenReturn(tradeList);
        when(apiController.getCandleSticks(any(), any())).thenReturn(candlestickList);

        dataProcessing.verifyAllCandleStick();
        verify(dataProcessing, times(2)).buildCandle(any(), any());
        verify(dataProcessing, times(1)).verifyCandle(any(), any());
        assertTrue(dataProcessing.verifyCandle(cc, c));
    }

    @Test
    public void testTradeBeforeCandleTime() {
        tradeList.add(new Trade(40000.00, 0.000046, "SELL", 2161703746513036512L, 1642090091961L, 1642090091961L, "BTC_USDT"));
        c = new Candlestick(1642090800000L, 41000.07, 41000.07, 41000.07, 41000.07, 255.31047);
        candlestickList.add(c);

        when(apiController.getTrades(any())).thenReturn(tradeList);
        when(apiController.getCandleSticks(any(), any())).thenReturn(candlestickList);

        dataProcessing.verifyAllCandleStick();
        verify(dataProcessing, times(0)).buildCandle(any(), any());
        verify(dataProcessing, times(0)).verifyCandle(any(), any());
    }

    @Test
    public void testInaccurateCandle() {
        tradeList.add(new Trade(42000.07, 0.000046, "SELL", 2161703746513036512L, 1642090900861L, 1642260591861L, "BTC_USDT"));
        tradeList.add(new Trade(43000.07, 0.000046, "SELL", 2161703746513036512L, 1642091091861L, 1642260591861L, "BTC_USDT"));
        c = new Candlestick(1642091100000L, 42000.07, 43000.07, 42000.07, 43000.07, 255.31047);
        candlestickList.add(c);
        cc = new CheckCandle(40000.07, 43000.07, 42000.07, 43000.07, 0);

        when(apiController.getTrades(any())).thenReturn(tradeList);
        when(apiController.getCandleSticks(any(), any())).thenReturn(candlestickList);

        dataProcessing.verifyAllCandleStick();
        verify(dataProcessing, times(2)).buildCandle(any(), any());
        verify(dataProcessing, times(1)).verifyCandle(any(), any());
        assertFalse(dataProcessing.verifyCandle(cc, c));
    }

}