package br.relatorio.binance.repository;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.relational.core.query.Criteria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.BooleanUtils.and;

public class RelatorioOrdemRepositoryCustomImpl implements RelatorioOrdemRepositoryCustom {

        @PersistenceContext
        private EntityManager em;

        public List<RelatorioOrdem> buscarOrdensFiltro(String orderNo, String status, String pairs, LocalDate data, Usuario usuario){
                CriteriaBuilder cb = em.getCriteriaBuilder();

                CriteriaQuery<RelatorioOrdem> cq = cb.createQuery(RelatorioOrdem.class);
                Root<RelatorioOrdem> root = cq.from(RelatorioOrdem.class);

                List<Predicate> predicates = new ArrayList<>();
                if ((orderNo != null) && (!orderNo.isEmpty())){
                        predicates.add(cb.equal(root.get("orderNo"),orderNo));
                }
                if ((pairs != null) && (!pairs.isEmpty())){
                        predicates.add(cb.equal(root.get("pair"), pairs));
                }
                if ((status != null) && (!status.isEmpty())){
                        predicates.add(cb.equal(root.get("status"),status));
                }
                if ((data != null)){
                        predicates.add(cb.between(root.get("dateUTC"), data.atStartOfDay(), data.plusDays(365).atStartOfDay()));
                }
                if (usuario != null){
                        predicates.add(cb.equal(root.get("usuario"), usuario));
                }
                cq.where(predicates.toArray(new Predicate[0]));

                return em.createQuery(cq).getResultList();
        }
}
