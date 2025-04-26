package br.relatorio.binance.controller;

import br.relatorio.binance.model.RelatorioNomad;
import br.relatorio.binance.repository.RelatorioNomadRepository;
import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.service.RelatorioNomadService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Relatório da Nomad", description = "Importação de arquivos PDF do corretora NOMAD")
@SecurityRequirement(name = "Bearer JWT")
public class RelatorioNomadImportController {

    @Autowired
    private RelatorioNomadRepository relatorioNomadRepository;

    @Autowired
    private RelatorioNomadService relatorioNomadService;

    @Operation(
            summary = "Importar ativos de um arquivo PDF",
            description = "Importa os dados dos ativos contidos em um arquivo PDF enviado pelo usuário."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Importação realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar o arquivo",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/importarNomad")
    public ResponseEntity<String> importarPdf(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            List<RelatorioNomad> dados = relatorioNomadService.extrairDados(arquivo);
            relatorioNomadRepository.saveAll(dados);
            return ResponseEntity.ok("Arquivo CSV importado com sucesso! O Total de Ordens importadas foi");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao importar o CSV: " + e.getMessage());
        }
    }

}
