package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioOrdemRepositoryCustom {

        List<RelatorioOrdem> buscarOrdensFiltro(String orderNo, String pairs, String status, LocalDate data, Usuario usuario);
}
