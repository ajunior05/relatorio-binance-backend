package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioNomad;
import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioNomadRepositoryCustomImpl implements RelatorioNomadRepositoryCustom {

        @PersistenceContext
        private EntityManager em;

        public List<RelatorioNomad> buscarOrdensFiltro(String symbol, String action, LocalDate data, Usuario usuario){
                CriteriaBuilder cb = em.getCriteriaBuilder();

                CriteriaQuery<RelatorioNomad> cq = cb.createQuery(RelatorioNomad.class);
                Root<RelatorioNomad> root = cq.from(RelatorioNomad.class);

                List<Predicate> predicates = new ArrayList<>();
                if ((symbol != null) && (!symbol.isEmpty())){
                        predicates.add(cb.equal(cb.lower(root.get("symbol")), symbol.toLowerCase()));
                }
                if ((action != null) && (!action.isEmpty())){
                        predicates.add(cb.equal(cb.lower(root.get("action")), action.toLowerCase()));
                }
                if ((data != null)){
                        predicates.add(cb.between(root.get("tradeDate"), data.atStartOfDay(), data.plusDays(365).atStartOfDay()));
                }
                if (usuario != null){
                        predicates.add(cb.equal(root.get("usuario"), usuario));
                }
                cq.where(predicates.toArray(new Predicate[0]));

                return em.createQuery(cq).getResultList();
        }
}
