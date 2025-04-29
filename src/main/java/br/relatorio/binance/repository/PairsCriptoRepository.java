package br.relatorio.binance.repository;

import br.relatorio.binance.model.PairsCripto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairsCriptoRepository extends PairsCriptoRepositoryCustom, JpaRepository<PairsCripto, Long> {}
