package com.eduardo.formulario.controller;

import com.eduardo.formulario.model.ItemVistoria;
import com.eduardo.formulario.model.Vistoria;
import com.eduardo.formulario.pdf.VistoriaPdfGenerator;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class VistoriaController {

    private static final Logger log = LoggerFactory.getLogger(VistoriaController.class);

    private static final List<String> SETORES = List.of(
            "Batch off & Embalagem",
            "Banbury's",
            "Negro de Fumo, Cargas Brancas e Plastificante",
            "Carrossel",
            "Backup"
    );

    private final VistoriaPdfGenerator pdfGenerator;
    private final String pastaDestino;

    public VistoriaController(VistoriaPdfGenerator pdfGenerator,
                               @Value("${vistoria.pdf.pasta-destino}") String pastaDestino) {
        this.pdfGenerator = pdfGenerator;
        this.pastaDestino = pastaDestino;
    }

    @GetMapping("/vistoria")
    public String mostrarFormulario(Model model) {
        Vistoria vistoria = new Vistoria();
        vistoria.setItens(SETORES.stream().map(ItemVistoria::new).toList());
        model.addAttribute("vistoria", vistoria);
        return "vistoria";
    }

    @PostMapping("/vistoria")
    public ResponseEntity<byte[]> receberFormulario(@ModelAttribute Vistoria vistoria) throws DocumentException, IOException {
        byte[] pdf = pdfGenerator.gerar(vistoria);

        String nomeArquivo = nomeArquivo(vistoria);
        try {
            Files.createDirectories(Path.of(pastaDestino));
            Files.write(Path.of(pastaDestino, nomeArquivo), pdf);
        } catch (IOException e) {
            log.warn("Nao foi possivel salvar o PDF na pasta de rede ({}). O download ainda sera oferecido ao usuario.", pastaDestino, e);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String nomeArquivo(Vistoria vistoria) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String responsavel = vistoria.getResponsavel() == null ? "" : vistoria.getResponsavel();
        String responsavelLimpo = responsavel.trim().replaceAll("[^a-zA-Z0-9]+", "_");
        return responsavelLimpo.isBlank()
                ? "vistoria_" + timestamp + ".pdf"
                : "vistoria_" + responsavelLimpo + "_" + timestamp + ".pdf";
    }
}