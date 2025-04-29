package br.relatorio.binance.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RelatorioNomad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String symbol;
    private String action;
    @Column(precision = 38, scale = 8)
    private BigDecimal quantity;
    @Column(precision = 38, scale = 8)
    private BigDecimal price;
    private LocalDate tradeDate;
    @Column(precision = 38, scale = 8)
    private BigDecimal netAmount;

    @Autowired
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public RelatorioNomad(Long id, String accountNumber,
                          String symbol, String action, BigDecimal quantity,
                          BigDecimal price, BigDecimal netAmount,
                          Usuario usuario, LocalDate tradeDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.symbol = symbol;
        this.action = action;
        this.quantity = quantity;
        this.price = price;
        this.usuario = usuario;
        this.tradeDate = tradeDate;
        this.netAmount = netAmount;
    }

    public RelatorioNomad() {

    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
