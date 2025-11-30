package com.hostal.api.controller;

import com.hostal.api.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@Tag(name = "Currency", description = "Servicios de conversión de monedas")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/rate")
    @Operation(summary = "Obtener tasa de cambio entre dos monedas")
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {
        
        BigDecimal rate = currencyService.getExchangeRate(from.toUpperCase(), to.toUpperCase());
        
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setFromCurrency(from.toUpperCase());
        response.setToCurrency(to.toUpperCase());
        response.setRate(rate);
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/convert")
    @Operation(summary = "Convertir cantidad de una moneda a otra")
    public ResponseEntity<ConversionResponse> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to) {
        
        BigDecimal convertedAmount = currencyService.convertCurrency(amount, from.toUpperCase(), to.toUpperCase());
        BigDecimal rate = currencyService.getExchangeRate(from.toUpperCase(), to.toUpperCase());
        
        ConversionResponse response = new ConversionResponse();
        response.setOriginalAmount(amount);
        response.setFromCurrency(from.toUpperCase());
        response.setToCurrency(to.toUpperCase());
        response.setConvertedAmount(convertedAmount);
        response.setExchangeRate(rate);
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rates/{baseCurrency}")
    @Operation(summary = "Obtener múltiples tasas de cambio desde una moneda base")
    public ResponseEntity<MultipleRatesResponse> getMultipleRates(@PathVariable String baseCurrency) {
        Map<String, BigDecimal> rates = currencyService.getMultipleRates(baseCurrency.toUpperCase());
        
        MultipleRatesResponse response = new MultipleRatesResponse();
        response.setBaseCurrency(baseCurrency.toUpperCase());
        response.setRates(rates);
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supported")
    @Operation(summary = "Obtener lista de monedas soportadas")
    public ResponseEntity<SupportedCurrenciesResponse> getSupportedCurrencies() {
        String[] currencies = currencyService.getSupportedCurrencies();
        
        SupportedCurrenciesResponse response = new SupportedCurrenciesResponse();
        response.setCurrencies(currencies);
        response.setSuccess(true);
        
        return ResponseEntity.ok(response);
    }

    // DTOs para respuestas
    public static class ExchangeRateResponse {
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal rate;
        private boolean success;

        // Getters y Setters
        public String getFromCurrency() { return fromCurrency; }
        public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
        
        public String getToCurrency() { return toCurrency; }
        public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
        
        public BigDecimal getRate() { return rate; }
        public void setRate(BigDecimal rate) { this.rate = rate; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    public static class ConversionResponse {
        private BigDecimal originalAmount;
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal convertedAmount;
        private BigDecimal exchangeRate;
        private boolean success;

        // Getters y Setters
        public BigDecimal getOriginalAmount() { return originalAmount; }
        public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
        
        public String getFromCurrency() { return fromCurrency; }
        public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
        
        public String getToCurrency() { return toCurrency; }
        public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
        
        public BigDecimal getConvertedAmount() { return convertedAmount; }
        public void setConvertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; }
        
        public BigDecimal getExchangeRate() { return exchangeRate; }
        public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    public static class MultipleRatesResponse {
        private String baseCurrency;
        private Map<String, BigDecimal> rates;
        private boolean success;

        // Getters y Setters
        public String getBaseCurrency() { return baseCurrency; }
        public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
        
        public Map<String, BigDecimal> getRates() { return rates; }
        public void setRates(Map<String, BigDecimal> rates) { this.rates = rates; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    public static class SupportedCurrenciesResponse {
        private String[] currencies;
        private boolean success;

        // Getters y Setters
        public String[] getCurrencies() { return currencies; }
        public void setCurrencies(String[] currencies) { this.currencies = currencies; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }
}