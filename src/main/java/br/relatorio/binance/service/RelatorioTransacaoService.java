package br.relatorio.binance.service;

import br.relatorio.binance.model.RelatorioTransacao;
import br.relatorio.binance.repository.RelatorioTransacaoRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioTransacaoService {

    @Autowired
    private RelatorioTransacaoRepository relatorioTransacaoRepository;

    public Integer importarCSV(Path caminhoDoArquivo) throws IOException {
        // Lê o arquivo CSV
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(caminhoDoArquivo.toFile()), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withQuote('"')
                    .parse(reader);

            Map<String, String> headerMap = new HashMap<>();

            List<String> rawHeaders = parser.getHeaderNames();
            for (String rawHeader : rawHeaders) {
                String cleanKey = rawHeader
                        .replace("\uFEFF", "")              // remove BOM
                        .replaceAll("^\"+|\"+$", "")        // remove aspas
                        .trim();
                headerMap.put(cleanKey, rawHeader);
            }

            //User_ID,"UTC_Time","Account","Operation","Coin","Change","Remark"
            int i = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (CSVRecord record : parser) {

               // if (Boolean.FALSE.equals(relatorioTransacaoRepository.findByOrderNoContaining(record.get("OrderNo")))){
                    // Atribui os valores de cada linha do CSV à entidade
                    RelatorioTransacao registro = new RelatorioTransacao();
                    registro.setUserId(Long.parseLong(record.get("User_ID")));
                    registro.setUtcTime(LocalDateTime.parse(record.get(headerMap.get("UTC_Time")), formatter));
                    registro.setAccount(record.get("Account"));
                    registro.setOperation(record.get("Operation"));
                    registro.setCoin(record.get("Coin"));
                    registro.setChange(parseBigDecimal(record.get("Change")));
                    registro.setRemark(record.get("Remark"));
                    // Salva o registro no banco de dados
                    relatorioTransacaoRepository.save(registro);
                    i++;
                //}
            }
            return i;
        }
    }

    private BigDecimal parseBigDecimal(String valorComUnidade) {
        if (valorComUnidade == null || valorComUnidade.isBlank()) {
            return BigDecimal.ZERO;
        }
        // Substitui traços incomuns por hífen normal
        String valorLimpo = valorComUnidade
                .replace("–", "-") // EN DASH
                .replace("−", "-") // traço matemático
                .replaceAll("[^\\d,.-]", "") // mantém apenas dígitos, vírgulas, pontos e hífen
                .replace(",", "."); // troca vírgula por ponto decimal
        if (valorLimpo.isBlank()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(valorLimpo);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter para BigDecimal: " + valorComUnidade + " => " + valorLimpo);
            return BigDecimal.ZERO;
        }
    }
}
