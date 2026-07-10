package com.eduardo.formulario.model;

import java.util.List;

public class Vistoria {

    private String responsavel;
    private List<ItemVistoria> itens;

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
}