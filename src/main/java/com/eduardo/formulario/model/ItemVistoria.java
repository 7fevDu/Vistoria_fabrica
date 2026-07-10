package com.eduardo.formulario.model;

public class ItemVistoria {

    private String setor;
    private String status; // "OK" ou "INCIDENTE"
    private String observacao;

    public ItemVistoria() {
    }

    public ItemVistoria(String setor) {
        this.setor = setor;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}