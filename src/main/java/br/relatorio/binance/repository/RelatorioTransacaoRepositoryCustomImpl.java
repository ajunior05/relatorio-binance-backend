package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.RelatorioTransacao;
import br.relatorio.binance.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioTransacaoRepositoryCustomImpl implements RelatorioTransacaoRepositoryCustom {

        @PersistenceContext
        private EntityManager em;

        public List<RelatorioTransacao> buscarOrdensFiltro(String coin, String operation, LocalDate data, Usuario usuario){
                CriteriaBuilder cb = em.getCriteriaBuilder();

                CriteriaQuery<RelatorioTransacao> cq = cb.createQuery(RelatorioTransacao.class);
                Root<RelatorioTransacao> root = cq.from(RelatorioTransacao.class);

                List<Predicate> predicates = new ArrayList<>();
                if ((coin != null) && (!coin.isEmpty())){
                        predicates.add(cb.equal(root.get("coin"),coin));
                }
                if ((operation != null) && (!operation.isEmpty())){
                        predicates.add(cb.equal(root.get("pair"), operation));
                }
                if ((data != null)){
                        predicates.add(cb.between(root.get("utcTime"), data.atStartOfDay(), data.plusDays(365).atStartOfDay()));
                }
                if (usuario != null){
                        predicates.add(cb.equal(root.get("usuario"), usuario));
                }
                cq.where(predicates.toArray(new Predicate[0]));

                return em.createQuery(cq).getResultList();
        }
}
