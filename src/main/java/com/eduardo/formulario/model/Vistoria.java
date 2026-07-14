package com.eduardo.formulario.model;

import java.util.List;

public class Vistoria {

    private String responsavel;
    private List<ItemVistoria> itens;
    private String assinaturaBase64;

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public List<ItemVistoria> getItens() {
        return itens;
    }

    public void setItens(List<ItemVistoria> itens) {
        this.itens = itens;
    }

    public String getAssinaturaBase64() {
        return assinaturaBase64;
    }

    public void setAssinaturaBase64(String assinaturaBase64) {
        this.assinaturaBase64 = assinaturaBase64;
    }
}