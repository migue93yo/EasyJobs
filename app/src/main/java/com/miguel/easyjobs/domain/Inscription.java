package com.miguel.easyjobs.domain;

import java.io.Serializable;

public class Inscription implements Serializable {

    private int id;
    private String email;
    private String state;
    public static final String INSCRITO = "Inscrito";
    public static final String SELECCIONADO = "Seleccionado";
    public static final String FINALIZADO = "Finalizado";

    public Inscription() {

    }

    public Inscription(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public Inscription(int id, String email, String state) {
        this.id = id;
        this.email = email;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
