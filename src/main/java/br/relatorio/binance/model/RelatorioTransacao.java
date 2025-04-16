package br.relatorio.binance.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class RelatorioTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDateTime utcTime;
    private String account;
    private String operation;
    private String coin;
    @Column(precision = 38, scale = 8)
    private BigDecimal change;
    private String remark;

    @Autowired
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public RelatorioTransacao(Long userId, LocalDateTime utcTime, String account, String operation, String coin, BigDecimal change, String remark) {
        this.userId = userId;
        this.utcTime = utcTime;
        this.account = account;
        this.operation = operation;
        this.coin = coin;
        this.change = change;
        this.remark = remark;
        this.usuario = new Usuario();
    }

    public RelatorioTransacao() {

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(LocalDateTime utcTime) {
        this.utcTime = utcTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
