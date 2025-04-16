package br.relatorio.binance.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
public class RelatorioOrdem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dateUTC;
    private String orderNo;
    private String pair;
    private String type;
    private String side;
    @Column(precision = 38, scale = 8)
    private BigDecimal orderPrice;
    @Column(precision = 38, scale = 8)
    private BigDecimal orderAmount;
    private String time;
    @Column(precision = 38, scale = 8)
    private BigDecimal executed;
    @Column(precision = 38, scale = 8)
    private BigDecimal averagePrice;
    @Column(precision = 38, scale = 8)
    private BigDecimal tradingTotal;
    private String status;

    @Autowired
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Construtor
    public RelatorioOrdem(LocalDateTime dateUTC, String orderNo, String pair, String type, String side,
                    BigDecimal orderPrice, BigDecimal orderAmount, String time, BigDecimal executed, 
                    BigDecimal averagePrice, BigDecimal tradingTotal, String status) {
        this.dateUTC = dateUTC;
        this.orderNo = orderNo;
        this.pair = pair;
        this.type = type;
        this.side = side;
        this.orderPrice = orderPrice;
        this.orderAmount = orderAmount;
        this.time = time;
        this.executed = executed;
        this.averagePrice = averagePrice;
        this.tradingTotal = tradingTotal;
        this.status = status;
        this.usuario = new Usuario();
    }

    public RelatorioOrdem() {

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Getters and Setters
    public LocalDateTime getDateUTC() {
        return dateUTC;
    }

    public void setDateUTC(LocalDateTime dateUTC) {
        this.dateUTC = dateUTC;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getExecuted() {
        return executed;
    }

    public void setExecuted(BigDecimal executed) {
        this.executed = executed;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTradingTotal() {
        return tradingTotal;
    }

    public void setTradingTotal(BigDecimal tradingTotal) {
        this.tradingTotal = tradingTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderPriceFormatado() {
        return orderPrice != null ? orderPrice.stripTrailingZeros().toPlainString() : "";
    }

    public String getOrderAmountFormatado() {
        return orderAmount != null ? orderAmount.stripTrailingZeros().toPlainString() : "";
    }

    public String getExecutedFormatado() {
        return executed != null ? executed.stripTrailingZeros().toPlainString() : "";
    }

    public String getAveragePriceFormatado() {
        return averagePrice != null ? averagePrice.stripTrailingZeros().toPlainString() : "";
    }

    public String getTradingTotalFormatado() {
        return tradingTotal != null ? tradingTotal.stripTrailingZeros().toPlainString() : "";
    }
}
