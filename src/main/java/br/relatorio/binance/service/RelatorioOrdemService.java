package br.relatorio.binance.service;

import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.repository.RelatorioOrdemRepository;
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
public class RelatorioOrdemService {

    @Autowired
    private RelatorioOrdemRepository relatorioOrdemRepository;

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
            int i = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (CSVRecord record : parser) {

                if (Boolean.FALSE.equals(relatorioOrdemRepository.findByOrderNoContaining(record.get("OrderNo")))){
                    // Atribui os valores de cada linha do CSV à entidade
                    RelatorioOrdem registro = new RelatorioOrdem();
                    registro.setAveragePrice(parseBigDecimal(record.get("Average Price")));
                    registro.setExecuted(parseBigDecimal(record.get("Executed")));
                    registro.setOrderAmount(parseBigDecimal(record.get("Order Amount")));
                    registro.setOrderPrice(parseBigDecimal(record.get("Order Price")));
                    registro.setOrderNo(record.get("OrderNo"));
                    registro.setPair(record.get("Pair"));
                    registro.setSide(record.get("Side"));
                    registro.setStatus(record.get("Status"));
                    registro.setTime(record.get("Time"));
                    registro.setTradingTotal(parseBigDecimal(record.get("Trading total")));
                    registro.setType(record.get("Type"));
                    registro.setDateUTC(LocalDateTime.parse(record.get(headerMap.get("Date(UTC)")), formatter));

                    // Converte o valor de 'data_nascimento' para LocalDate

                    // Salva o registro no banco de dados
                    relatorioOrdemRepository.save(registro);
                    i++;
                }
            }
            return i;
        }
    }


    private BigDecimal parseBigDecimal(String valorComUnidade) {
        if (valorComUnidade == null || valorComUnidade.isEmpty()) {
            return BigDecimal.ZERO;
        }
        // Remove tudo que não for número, ponto ou vírgula (ex: "0.01269XRP" → "0.01269")
        String valorLimpo = valorComUnidade.replaceAll("[^\\d.,-]", "");
        // Substitui vírgula por ponto, caso venha com separador europeu
        valorLimpo = valorLimpo.replace(",", ".");
        if (valorLimpo.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(valorLimpo);
    }
}
