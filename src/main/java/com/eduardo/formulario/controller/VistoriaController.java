package com.eduardo.formulario.controller;

import com.eduardo.formulario.model.ItemVistoria;
import com.eduardo.formulario.model.Vistoria;
import com.eduardo.formulario.pdf.VistoriaPdfGenerator;
import com.lowagie.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class VistoriaController {

    private static final List<String> SETORES = List.of(
            "Batch off & Embalagem",
            "Banbury's",
            "Negro de Fumo, Cargas Brancas e Plastificante",
            "Carrossel",
            "Backup"
    );

    private final VistoriaPdfGenerator pdfGenerator;

    public VistoriaController(VistoriaPdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
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

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vistoria.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}