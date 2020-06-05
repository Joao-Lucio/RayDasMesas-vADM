package com.example.raydasmesas_28_01_2020.domain;


import java.io.Serializable;
import java.util.Date;

public class Promocao implements Serializable {

    private String nome;
    private Date data_inic;
    private Date data_fim;
    private String descricao;
    private int desconto;
    private int quantidade;
    private String tipo;
    private String uid;

    public void setDesconto(int desconto) {
        this.desconto = desconto;
    }

    public void setData_fim(Date data) {
        this.data_fim = data;
    }

    public void setData_inic(Date data) {
        this.data_inic = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setQuantidade(int qtd) {
        this.quantidade = qtd;
    }

    public int getDesconto() {
        return this.desconto;
    }

    public Date getData_fim() {
        return this.data_fim;
    }

    public Date getData_inic() {
        return this.data_inic;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getNome() {
        return this.nome;
    }

    public String getTipo() {
        return this.tipo;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
