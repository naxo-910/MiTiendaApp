package com.hostal.api.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyService {

    @Value("${currency.api.key:demo}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";

    public CurrencyService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Obtener tasa de cambio entre dos monedas
     */
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String url = BASE_URL + "/" + apiKey + "/pair/" + fromCurrency + "/" + toCurrency;
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            
            if (response != null && "success".equals(response.getResult())) {
                return response.getConversionRate();
            } else {
                // En caso de error, usar tasas por defecto
                return getDefaultRate(fromCurrency, toCurrency);
            }
        } catch (RestClientException e) {
            // En caso de error de conexión, usar tasas por defecto
            return getDefaultRate(fromCurrency, toCurrency);
        }
    }

    /**
     * Convertir cantidad de una moneda a otra
     */
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        
        BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Obtener tasas de cambio múltiples desde una moneda base
     */
    public Map<String, BigDecimal> getMultipleRates(String baseCurrency) {
        try {
            String url = BASE_URL + "/" + apiKey + "/latest/" + baseCurrency;
            LatestRatesResponse response = restTemplate.getForObject(url, LatestRatesResponse.class);
            
            if (response != null && "success".equals(response.getResult())) {
                return response.getConversionRates();
            } else {
                return getDefaultRates(baseCurrency);
            }
        } catch (RestClientException e) {
            return getDefaultRates(baseCurrency);
        }
    }

    /**
     * Obtener información de monedas soportadas
     */
    public String[] getSupportedCurrencies() {
        return new String[]{
            "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", 
            "SEK", "NZD", "MXN", "SGD", "HKD", "NOK", "TRY", "RUB",
            "INR", "BRL", "ZAR", "KRW", "ARS", "CLP", "COP", "PEN"
        };
    }

    /**
     * Obtener tasas por defecto en caso de error de API
     */
    private BigDecimal getDefaultRate(String fromCurrency, String toCurrency) {
        // Tasas aproximadas para casos de emergencia
        if ("USD".equals(fromCurrency)) {
            switch (toCurrency) {
                case "EUR": return new BigDecimal("0.85");
                case "GBP": return new BigDecimal("0.73");
                case "JPY": return new BigDecimal("110.0");
                case "ARS": return new BigDecimal("350.0");
                case "CLP": return new BigDecimal("800.0");
                case "COP": return new BigDecimal("4000.0");
                case "PEN": return new BigDecimal("3.7");
                case "BRL": return new BigDecimal("5.2");
                default: return BigDecimal.ONE;
            }
        } else if ("EUR".equals(fromCurrency) && "USD".equals(toCurrency)) {
            return new BigDecimal("1.18");
        }
        
        return BigDecimal.ONE; // Tasa 1:1 por defecto
    }

    /**
     * Obtener múltiples tasas por defecto
     */
    private Map<String, BigDecimal> getDefaultRates(String baseCurrency) {
        return Map.of(
            "USD", new BigDecimal("1.0"),
            "EUR", new BigDecimal("0.85"),
            "GBP", new BigDecimal("0.73"),
            "JPY", new BigDecimal("110.0"),
            "ARS", new BigDecimal("350.0"),
            "CLP", new BigDecimal("800.0")
        );
    }

    // DTOs para respuestas de la API
    public static class ExchangeRateResponse {
        private String result;
        @JsonProperty("conversion_rate")
        private BigDecimal conversionRate;

        // Getters y Setters
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        
        public BigDecimal getConversionRate() { return conversionRate; }
        public void setConversionRate(BigDecimal conversionRate) { this.conversionRate = conversionRate; }
    }

    public static class LatestRatesResponse {
        private String result;
        @JsonProperty("conversion_rates")
        private Map<String, BigDecimal> conversionRates;

        // Getters y Setters
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        
        public Map<String, BigDecimal> getConversionRates() { return conversionRates; }
        public void setConversionRates(Map<String, BigDecimal> conversionRates) { this.conversionRates = conversionRates; }
    }
}