package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioNomad;
import br.relatorio.binance.model.RelatorioOrdem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioNomadRepository extends JpaRepository<RelatorioNomad, Long> {


}
