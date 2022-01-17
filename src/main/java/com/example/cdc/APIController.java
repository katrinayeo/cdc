package com.example.cdc;

import com.example.cdc.Model.Candlestick;
import com.example.cdc.Model.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class APIController {

    @Autowired
    RestTemplate restTemplate;

    public APIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*    @EventListener(ApplicationReadyEvent.class)
    public void call() {
        System.out.println("running");
        getTrades("BTC_USDT");
        getCandleSticks("1m", "BTC_USDT");
    }*/

    private final String baseUrl = "https://api.crypto.com/v2/";
    private final String getTradesURL = "public/get-trades";
    private final String getCandleStickURL = "public/get-candlestick";

    //returns time sorted list of trades from get-trades API
    public List<Trade> getTrades(String instrument_name) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(baseUrl + getTradesURL)
                .queryParam("instrument_name", instrument_name)
                .encode()
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlTemplate, String.class);

        //TradeResponse tradeResponse = responseEntity.getBody();
        List<Trade> tradeList = null;
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            String body = responseEntity.getBody();
            ObjectMapper mapper = new ObjectMapper();
            try {
                tradeList = mapper.readValue(body.substring(body.indexOf("[")), new TypeReference<List<Trade>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return tradeList.stream().sorted(Comparator.comparingLong(Trade::getT)).collect(Collectors.toList());
    }

    //returns time sorted list of candles stick from get-candlestick API
    public List<Candlestick> getCandleSticks(String timeframe, String instrument_name) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(baseUrl + getCandleStickURL)
                .queryParam("timeframe", timeframe)
                .queryParam("instrument_name", instrument_name)
                .encode()
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlTemplate, String.class);

        //TradeResponse tradeResponse = responseEntity.getBody();
        List<Candlestick> candlestickList = null;
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            String body = responseEntity.getBody();
            ObjectMapper mapper = new ObjectMapper();
            try {
                candlestickList = mapper.readValue(body.substring(body.indexOf("[")), new TypeReference<List<Candlestick>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return candlestickList.stream().sorted(Comparator.comparingLong(Candlestick::getT)).collect(Collectors.toList());
    }


}
