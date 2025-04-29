package br.relatorio.binance.repository;

import br.relatorio.binance.model.PairsCripto;
import br.relatorio.binance.model.RelatorioNomad;
import br.relatorio.binance.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface PairsCriptoRepositoryCustom {

        List<PairsCripto> buscarPairsFiltro(String baseCurrancy, String pair, String quoteCurrancy);
}