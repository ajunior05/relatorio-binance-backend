package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.RelatorioTransacao;
import br.relatorio.binance.model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioTransacaoRepositoryCustom {

        List<RelatorioTransacao> buscarOrdensFiltro(String coin, String operation, LocalDate data, Usuario usuario);
}