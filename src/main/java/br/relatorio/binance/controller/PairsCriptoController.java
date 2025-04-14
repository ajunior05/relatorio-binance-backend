package br.relatorio.binance.controller;

import br.relatorio.binance.model.PairsCripto;
import br.relatorio.binance.repository.PairsCriptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


public class PairsCriptoController {


    private PairsCriptoRepository pairsCriptoRepository;

    public List<PairsCripto> findAllPairsCripto(){
        return pairsCriptoRepository.findAll();
    }
}
