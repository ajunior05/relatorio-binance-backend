package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioNomad;
import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioNomadRepositoryCustom {

        List<RelatorioNomad> buscarOrdensFiltro(String symbol, String action, LocalDate data, Usuario usuario);
}
