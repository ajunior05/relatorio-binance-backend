package br.relatorio.binance.controller;

import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.service.RelatorioOrdemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
public class RelatorioOrdemImportController {

    @Autowired
    private RelatorioOrdemService relatorioOrdemService;

    @Autowired
    private RelatorioOrdemRepository relatorioOrdemRepository;

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
