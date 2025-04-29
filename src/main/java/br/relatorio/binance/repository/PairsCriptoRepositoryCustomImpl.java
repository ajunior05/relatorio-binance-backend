package br.relatorio.binance.repository;

import br.relatorio.binance.model.PairsCripto;
import br.relatorio.binance.model.RelatorioNomad;
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

public class PairsCriptoRepositoryCustomImpl implements PairsCriptoRepositoryCustom {

        @PersistenceContext
        private EntityManager em;
        public List<PairsCripto> buscarPairsFiltro(String baseCurrancy, String pair, String quoteCurrancy){
                CriteriaBuilder cb = em.getCriteriaBuilder();

                CriteriaQuery<PairsCripto> cq = cb.createQuery(PairsCripto.class);
                Root<PairsCripto> root = cq.from(PairsCripto.class);

                List<Predicate> predicates = new ArrayList<>();
                if ((baseCurrancy != null) && (!baseCurrancy.isEmpty())){
                        predicates.add(cb.equal(cb.lower(root.get("baseCurrancy")), baseCurrancy.toLowerCase()));
                }
                if ((pair != null) && (!pair.isEmpty())){
                        predicates.add(cb.equal(cb.lower(root.get("pair")), pair.toLowerCase()));
                }
                if ((quoteCurrancy != null) && (!quoteCurrancy.isEmpty())){
                        predicates.add(cb.equal(cb.lower(root.get("quoteCurrancy")), quoteCurrancy.toLowerCase()));
                }
                cq.where(predicates.toArray(new Predicate[0]));

                return em.createQuery(cq).getResultList();
        }
}
