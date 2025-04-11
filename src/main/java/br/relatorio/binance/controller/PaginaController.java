package br.relatorio.binance.controller;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.RelatorioTransacao;
import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.repository.RelatorioTransacaoRepository;
import br.relatorio.binance.service.RelatorioOrdemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Relatório", description = "Consultas e Upload de Ordens e Transações")
public class PaginaController {

    @Autowired
    private RelatorioOrdemService relatorioOrdemService;

    @Autowired
    private RelatorioOrdemRepository relatorioOrdemRepository;

    @Autowired
    private RelatorioTransacaoRepository relatorioTransacaoRepository;

    @Operation(summary = "Consultar Ordens", description = "Retorna uma lista de ordens com base nos filtros: orderNo, status ou data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
    })
    @GetMapping("/consultarOrdem")
    public List<RelatorioOrdem> consultarOrdens(
            @Parameter(description = "Número da ordem") @RequestParam(required = false) String orderNo,
            @Parameter(description = "Status da ordem") @RequestParam(required = false, defaultValue = "") String status,
            @Parameter(description = "Data da ordem (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {


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

    @Operation(summary = "Consultar Transações", description = "Retorna uma lista de transações com base nos filtros: coin, operation ou data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
    })
    @GetMapping("/consultarTransacao")
    public List<RelatorioTransacao> consultarTransacoes(
            @Parameter(description = "Moeda da transação") @RequestParam(required = false) String coin,
            @Parameter(description = "Tipo de operação") @RequestParam(required = false, defaultValue = "") String operation,
            @Parameter(description = "Data da transação (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {


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


    @Operation(summary = "Upload de Arquivo CSV", description = "Realiza o upload e importação de um arquivo CSV contendo ordens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload realizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar o arquivo")
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(
            @Parameter(description = "Arquivo CSV de ordens") @RequestParam("file") MultipartFile file) {

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