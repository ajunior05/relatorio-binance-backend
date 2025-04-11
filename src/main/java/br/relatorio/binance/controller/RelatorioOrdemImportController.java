package br.relatorio.binance.controller;

import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.service.RelatorioOrdemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
@Tag(name = "Relatório Transação", description = "Importação de arquivos CSV de transações")
@SecurityRequirement(name = "Bearer JWT")
public class RelatorioOrdemImportController {

    @Autowired
    private RelatorioOrdemService relatorioOrdemService;

    @Autowired
    private RelatorioOrdemRepository relatorioOrdemRepository;

    @Operation(
            summary = "Importar ordens de um arquivo CSV",
            description = "Importa os dados das ordens contidos em um arquivo CSV enviado pelo usuário."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Importação realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar o arquivo",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/importarOrdem")
    public ResponseEntity<String> importarCSV(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            // Salvar o arquivo temporariamente
            Path tempFile = Files.createTempFile("upload-", ".csv");
            arquivo.transferTo(tempFile.toFile());

            // Importar o CSV
            String qtde = String.valueOf(relatorioOrdemService.importarCSV(tempFile));

            // Deletar o arquivo temporário
            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok("Arquivo CSV importado com sucesso! O Total de Ordens importadas foi: "+ qtde);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao importar o CSV: " + e.getMessage());
        }
    }

}
