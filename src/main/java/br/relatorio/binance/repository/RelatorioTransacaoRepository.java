package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioTransacaoRepository extends JpaRepository<RelatorioTransacao, Long> {

        List<RelatorioTransacao> findByCoin(String coin);
        List<RelatorioTransacao> findByOperation(String operation);
        List<RelatorioTransacao> findByUtcTime(LocalDateTime data);
}
