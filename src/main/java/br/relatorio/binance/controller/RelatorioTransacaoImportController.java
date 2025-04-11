package br.relatorio.binance.controller;

import br.relatorio.binance.repository.RelatorioTransacaoRepository;
import br.relatorio.binance.service.RelatorioTransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
@Tag(name = "Relatório Transação", description = "Importação de arquivos CSV de transações")
@SecurityRequirement(name = "Bearer JWT")
public class RelatorioTransacaoImportController {

    @Autowired
    private RelatorioTransacaoService relatorioTransacaoService;

    @Autowired
    private RelatorioTransacaoRepository relatorioTransacaoRepository;

    @Operation(
            summary = "Importar transações de um arquivo CSV",
            description = "Importa os dados de transações contidos em um arquivo CSV enviado pelo usuário."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Importação realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar o arquivo",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/importarTransacao")
    public ResponseEntity<String> importarCSV(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            // Salvar o arquivo temporariamente
            Path tempFile = Files.createTempFile("upload-", ".csv");
            arquivo.transferTo(tempFile.toFile());
            // Importar o CSV
            String qtde = String.valueOf(relatorioTransacaoService.importarCSV(tempFile));
            // Deletar o arquivo temporário
            Files.deleteIfExists(tempFile);
            return ResponseEntity.ok("Arquivo CSV importado com sucesso! O Total de Transações importadas foi: "+ qtde);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao importar o CSV: " + e.getMessage());
        }
    }

}
