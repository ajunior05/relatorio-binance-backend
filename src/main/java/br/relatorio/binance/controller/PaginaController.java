package br.relatorio.binance.controller;

import br.relatorio.binance.model.*;
import br.relatorio.binance.repository.PairsCriptoRepository;
import br.relatorio.binance.repository.RelatorioNomadRepository;
import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.repository.RelatorioTransacaoRepository;
import br.relatorio.binance.service.RelatorioNomadService;
import br.relatorio.binance.service.RelatorioOrdemService;
import br.relatorio.binance.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
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

    @Autowired
    private RelatorioNomadRepository relatorioNomadRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RelatorioNomadService relatorioNomadService;

    @Autowired
    private PairsCriptoRepository pairsCriptoRepository;

    @Operation(summary = "Consultar Ordens", description = "Retorna uma lista de ordens com base nos filtros: orderNo, status ou data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
    })
    @GetMapping("/consultarOrdem")
    public List<RelatorioOrdem> consultarOrdens(
            @Parameter(description = "Número da ordem") @RequestParam(required = false) String orderNo,
            @Parameter(description = "Status da ordem") @RequestParam(required = false, defaultValue = "") String status,
            @Parameter(description = "Lista de pares") @RequestParam(required = false, defaultValue = "") String pairs,
            @Parameter(description = "Data da ordem (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        if ((orderNo != null)||(status != null)||(pairs != null)||(data != null)){
            return relatorioOrdemRepository.buscarOrdensFiltro(orderNo,status,pairs, data, usuario);
        } else{
            return Collections.emptyList();
        }
    }

    @Operation(summary = "Consultar Relatório da Nomad", description = "Retorna uma lista de ativos com base nos filtros: symbol,action e data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
    })
    @GetMapping("/consultarNomad")
    public List<RelatorioNomad> consultarNomad(
            @Parameter(description = "Ativo comprado") @RequestParam(required = false) String symbol,
            @Parameter(description = "tipo de transação") @RequestParam(required = false, defaultValue = "") String action,
            @Parameter(description = "Data da compra (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        if ((symbol != null)||(action != null)||(data != null)){
            return relatorioNomadRepository.buscarOrdensFiltro(symbol,action, data, usuario);
        } else{
            return Collections.emptyList();
        }
    }

    @Operation(summary = "Consultar Pares", description = "Retorna uma lista de pares com base nos filtros: baseCurrancy,pair e quoteCurrancy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
    })
    @GetMapping("/consultarPares")
    public List<PairsCripto> consultarPares(
            @Parameter(description = "Ativo comprado") @RequestParam(required = false) String baseCurrancy,
            @Parameter(description = "tipo de transação") @RequestParam(required = false, defaultValue = "") String pair,
            @Parameter(description = "tipo de transação") @RequestParam(required = false, defaultValue = "") String quoteCurrancy) {
        if ((baseCurrancy != null)||(pair != null)||(quoteCurrancy != null)){
            return pairsCriptoRepository.buscarPairsFiltro(baseCurrancy, pair, quoteCurrancy);
        } else{
            return Collections.emptyList();
        }
    }

    @Operation(summary = "Consultar pares", description = "Retorna uma lista de pares.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
    })
    @GetMapping("/pairs")
    public List<PairsCripto> findAllPairsCripto(){
        return pairsCriptoRepository.findAll();
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


        Usuario usuario = usuarioService.getUsuarioLogado();

        if ((coin != null)||(operation != null)||(data != null)){
            return relatorioTransacaoRepository.buscarOrdensFiltro(coin,operation,data,usuario);
        } else{
            return Collections.emptyList();
        }
        /*if (coin != null && !coin.isEmpty()) {
            return relatorioTransacaoRepository.findByCoin(coin);
        } else if (data != null) {
            return relatorioTransacaoRepository.findByUtcTime(
                    data.atStartOfDay());
        } else if (operation != null && !operation.isEmpty()) {
            return relatorioTransacaoRepository.findByOperation(operation);
        } else {
            return relatorioTransacaoRepository.findAll();

        }*/
    }


   /* @Operation(summary = "Upload de Arquivo CSV", description = "Realiza o upload e importação de um arquivo CSV contendo ordens.")
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
    }*/
/*    @PostMapping("/uploadNomad")
    public ResponseEntity<String> uploadPdfNomad(
            @Parameter(description = "Arquivo PDF da Nomad") @RequestParam("file") MultipartFile file) {

        try {
            File tempFile = File.createTempFile("uploadNomad", ".pdf");
            file.transferTo(tempFile);
            relatorioNomadService.extrairTextoDoPdf(file);
            return ResponseEntity.ok("Arquivo importado com sucesso!");

*//*            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);
            document.close();
            return texto;*//*
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }*/
}