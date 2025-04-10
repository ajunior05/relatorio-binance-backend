package br.relatorio.binance.controller;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.RelatorioTransacao;
import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.repository.RelatorioTransacaoRepository;
import br.relatorio.binance.service.RelatorioOrdemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaginaController {

    @Autowired
    private RelatorioOrdemService relatorioOrdemService;

    @Autowired
    private RelatorioOrdemRepository relatorioOrdemRepository;

    @Autowired
    private RelatorioTransacaoRepository relatorioTransacaoRepository;

    @GetMapping("/consultarOrdem")
    public List<RelatorioOrdem> consultarOrdens(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        if (orderNo != null && !orderNo.isEmpty()) {
            return relatorioOrdemRepository.findByOrderNoContaining(orderNo);
        } else if (data != null) {
            return relatorioOrdemRepository.findByDateUTCBetween(
                    data.atStartOfDay(), data.plusDays(365).atStartOfDay());
        } else if (status != null && !status.isEmpty()) {
            return relatorioOrdemRepository.findByStatus(status);
        } else {
            return relatorioOrdemRepository.findAll();
        }
    }

    @GetMapping("/consultarTransacao")
    public List<RelatorioTransacao> consultarTransacoes(
            @RequestParam(required = false) String coin,
            @RequestParam(required = false, defaultValue = "") String operation,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        if (coin != null && !coin.isEmpty()) {
            return relatorioTransacaoRepository.findByCoin(coin);
        } else if (data != null) {
            return relatorioTransacaoRepository.findByUtcTime(
                    data.atStartOfDay());
        } else if (operation != null && !operation.isEmpty()) {
            return relatorioTransacaoRepository.findByOperation(operation);
        } else {
            List<RelatorioTransacao> teste =relatorioTransacaoRepository.findAll();
            return teste;
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload", ".csv");
            file.transferTo(tempFile);
            relatorioOrdemService.importarCSV(tempFile.toPath());
            return ResponseEntity.ok("Arquivo importado com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }
}