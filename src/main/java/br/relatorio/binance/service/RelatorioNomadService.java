package br.relatorio.binance.service;

import br.relatorio.binance.model.RelatorioNomad;
import br.relatorio.binance.model.RelatorioOrdem;
import br.relatorio.binance.model.Usuario;
import br.relatorio.binance.repository.RelatorioOrdemRepository;
import br.relatorio.binance.repository.UsuarioRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RelatorioNomadService {

    @Autowired
    private RelatorioOrdemRepository relatorioOrdemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    private List<Map<String, String>> extrairDadosTabularesIgnorandoCampos(String texto) {
        List<Map<String, String>> resultados = new ArrayList<>();

        List<String> linhas = Arrays.stream(texto.split("\n"))
                .map(String::trim)
                .filter(l -> !l.isBlank())
                .collect(Collectors.toList());

        // Identificar o cabeçalho
        int cabecalhoIndex = -1;
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i).toLowerCase();
            if (linha.contains("symbol") && linha.contains("quantity") && linha.contains("price")) {
                cabecalhoIndex = i;
                break;
            }
        }

        if (cabecalhoIndex == -1 || cabecalhoIndex + 1 >= linhas.size()) {
            System.out.println("Cabeçalho não encontrado.");
            return resultados;
        }
        // Separar cabeçalhos
        String[] cabecalhosBrutos = linhas.get(cabecalhoIndex).split("\\s{1,}");
        Set<String> camposIgnorados = Set.of("Security", "A/CType", "Action", "Capacity", "Trade", "Date", "Settle");
        List<String> cabecalhosFiltrados = new ArrayList<>();

        for (int i = 0; i < cabecalhosBrutos.length; i++) {
            if (!camposIgnorados.contains(cabecalhosBrutos[i].trim())) {
                cabecalhosFiltrados.add(cabecalhosBrutos[i].trim());
            }
            if (cabecalhosBrutos[i].trim().equals("Trade")) {
                cabecalhosFiltrados.add("Trade Date");
            }
        }

        // Agora ler TODAS as linhas abaixo do cabeçalho
        for (int i = cabecalhoIndex + 1; i < linhas.size(); i++) {
            String linhaAtual = linhas.get(i);
            if (linhaAtual.toLowerCase().contains("page")){
                i = i + 11;
                continue;
            }
            if (linhaAtual.toLowerCase().contains("transaction terms")){
                break;
            }
            // Ignora linhas que são títulos como "Principal Amount" ou "Net Amount"
            if (linhaAtual.toLowerCase().contains("principal amount") ||
                    linhaAtual.toLowerCase().contains("interest") ||
                    linhaAtual.toLowerCase().contains("net amount") ||
                    linhaAtual.toLowerCase().contains("commission") ||
                    linhaAtual.toLowerCase().contains("transaction fee") ||
                    linhaAtual.toLowerCase().contains("other fees")) {
                continue;
            }
            // Separar valores
            String[] valores = linhaAtual.split("\\s{1,}");
            List<String> valoresFiltrados = new ArrayList<>();
            valoresFiltrados.add(valores[0].trim()); // Symbol
            boolean encontrouPrimeiraData = false;
            for (int j = 1; j < valores.length; j++) {
                if (valores[j].matches("-?\\d+\\.\\d+")) { // Números (quantity, price)
                    valoresFiltrados.add(valores[j]);
                } else if (valores[j].matches("\\d{1,2}/\\d{1,2}/\\d{2,4}") && !encontrouPrimeiraData) { // primeira data (Trade Date)
                    encontrouPrimeiraData = true;
                    valoresFiltrados.add(valores[j]);
                }
            }
            if (!valoresFiltrados.isEmpty()) {
                Map<String, String> registro = new LinkedHashMap<>();
                for (int k = 0; k < Math.min(cabecalhosFiltrados.size(), valoresFiltrados.size()); k++) {
                    registro.put(cabecalhosFiltrados.get(k), valoresFiltrados.get(k));
                }
                resultados.add(registro);
            }
        }
        return resultados;
    }

    public List<RelatorioNomad> extrairDados(MultipartFile pdfFile) throws IOException {
        List<RelatorioNomad> registros = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper. getText(document);
            Usuario usuario = usuarioService.getUsuarioLogado();
            List<Map<String, String>> valores = extrairDadosTabularesIgnorandoCampos(texto);

            for (Map<String, String> valor : valores) {
                RelatorioNomad transacao = new RelatorioNomad();
                if (valor.containsKey("Symbol")){
                    transacao.setSymbol(valor.get("Symbol"));
                }
                if (valor.containsKey("Quantity")){
                    transacao.setQuantity(new BigDecimal(valor.get("Quantity")));
                }
                if (valor.containsKey("Price")){
                    transacao.setPrice(new BigDecimal(valor.get("Price")));
                }
                if (valor.containsKey("Trade Date")){
                    String tradeDateStr = valor.get("Trade Date");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    LocalDate tradeDate = LocalDate.parse(tradeDateStr, formatter);
                    transacao.setTradeDate(tradeDate);
                }
                if ((transacao.getPrice() != null) && (transacao.getQuantity() != null)){
                    transacao.setNetAmount(transacao.getPrice().multiply(transacao.getQuantity()));
                }
                transacao.setUsuario(usuario);
                registros.add(transacao);
            }
        }

        return registros;
    }

}
