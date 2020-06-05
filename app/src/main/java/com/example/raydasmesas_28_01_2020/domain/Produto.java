package com.example.raydasmesas_28_01_2020.domain;

import java.io.Serializable;

public class Produto implements Serializable {

    private int quantidade;
    private String tipo;
    private String caracteristica;
    private String uid;
    private String photo;

    public Produto(){

    }

    public Produto(String im, String carac, String ti, int qtd, String uid){
        this.quantidade = qtd;
        this.tipo = ti;
        this.caracteristica = carac;
        this.uid = uid;
        this.photo = im;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public String getCaracteristica() {
        return this.caracteristica;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
