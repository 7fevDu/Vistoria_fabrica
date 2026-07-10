package com.eduardo.formulario.pdf;

import com.eduardo.formulario.model.ItemVistoria;
import com.eduardo.formulario.model.Vistoria;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VistoriaPdfGenerator {

    private static final Color AMARELO = new Color(245, 197, 24);
    private static final Color CINZA_ESCURO = new Color(43, 43, 43);
    private static final Color CINZA_CLARO = new Color(240, 240, 240);
    private static final Color VERDE = new Color(22, 163, 74);
    private static final Color VERMELHO = new Color(220, 38, 38);
    private static final Color BRANCO = Color.WHITE;

    public byte[] gerar(Vistoria vistoria) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 40, 40, 50, 40);
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, saida);
        document.open();

        BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont baseFontBold = BaseFont.createFont("C:/Windows/Fonts/calibrib.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font tituloFont = new Font(baseFontBold, 20, Font.NORMAL, CINZA_ESCURO);
        Font subtituloFont = new Font(baseFont, 10, Font.NORMAL, new Color(107, 107, 107));
        Font cabecalhoTabelaFont = new Font(baseFontBold, 10, Font.NORMAL, BRANCO);
        Font textoFont = new Font(baseFont, 10, Font.NORMAL, CINZA_ESCURO);
        Font statusFont = new Font(baseFontBold, 10, Font.NORMAL, BRANCO);

        adicionarLogoSePossivel(document);

        document.add(new Paragraph("Relatório de Vistoria", tituloFont));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        document.add(new Paragraph("Gerado em " + LocalDateTime.now().format(formatter), subtituloFont));
        document.add(new Paragraph(" "));

        document.add(criarLinhaSeparadora());
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Responsável: " + vistoria.getResponsavel(), textoFont));
        document.add(new Paragraph(" "));

        PdfPTable tabela = new PdfPTable(new float[]{3f, 1.4f, 3.6f});
        tabela.setWidthPercentage(100);

        adicionarCabecalho(tabela, "Setor", cabecalhoTabelaFont);
        adicionarCabecalho(tabela, "Status", cabecalhoTabelaFont);
        adicionarCabecalho(tabela, "Observação", cabecalhoTabelaFont);

        for (ItemVistoria item : vistoria.getItens()) {
            PdfPCell celulaSetor = new PdfPCell(new Phrase(item.getSetor(), textoFont));
            celulaSetor.setPadding(8);
            celulaSetor.setBackgroundColor(CINZA_CLARO);
            tabela.addCell(celulaSetor);

            boolean ok = "OK".equals(item.getStatus());
            PdfPCell celulaStatus = new PdfPCell(new Phrase(ok ? "OK" : "INCIDENTE", statusFont));
            celulaStatus.setBackgroundColor(ok ? VERDE : VERMELHO);
            celulaStatus.setPadding(8);
            celulaStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabela.addCell(celulaStatus);

            String observacao = (item.getObservacao() == null || item.getObservacao().isBlank())
                    ? "-" : item.getObservacao();
            PdfPCell celulaObs = new PdfPCell(new Phrase(observacao, textoFont));
            celulaObs.setPadding(8);
            tabela.addCell(celulaObs);
        }

        document.add(tabela);

        document.close();

        return saida.toByteArray();
    }

    private void adicionarLogoSePossivel(Document document) {
        try {
            Image logo = Image.getInstance(new ClassPathResource("static/logo.png").getURL());
            logo.scaleToFit(80, 80);
            document.add(logo);
        } catch (Exception e) {
            // Sem logo ainda, ou arquivo não encontrado: segue sem ela.
        }
    }

    private PdfPTable criarLinhaSeparadora() {
        PdfPTable linha = new PdfPTable(1);
        linha.setWidthPercentage(100);
        PdfPCell celula = new PdfPCell();
        celula.setBackgroundColor(AMARELO);
        celula.setFixedHeight(3f);
        celula.setBorder(Rectangle.NO_BORDER);
        linha.addCell(celula);
        return linha;
    }

    private void adicionarCabecalho(PdfPTable tabela, String texto, Font fonte) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonte));
        celula.setBackgroundColor(CINZA_ESCURO);
        celula.setPadding(8);
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabela.addCell(celula);
    }
}