package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioOrdem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioOrdemRepository extends JpaRepository<RelatorioOrdem, Long> {

        List<RelatorioOrdem> findByOrderNoContaining(String orderNo);
        Boolean findByOrderNo(String orderNo);
        List<RelatorioOrdem> findByStatus(String status);
        List<RelatorioOrdem> findByDateUTCBetween(LocalDateTime start, LocalDateTime end);
}
