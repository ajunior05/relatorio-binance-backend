package br.relatorio.binance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PairsCripto {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String pair;
private String baseCurrency;
private String quoteCurrancy;

    public PairsCripto(Long id, String pair, String baseCurrency, String quoteCurrancy) {
        this.id = id;
        this.pair = pair;
        this.baseCurrency = baseCurrency;
        this.quoteCurrancy = quoteCurrancy;
    }

    public PairsCripto(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrancy() {
        return quoteCurrancy;
    }

    public void setQuoteCurrancy(String quoteCurrancy) {
        this.quoteCurrancy = quoteCurrancy;
    }
}
