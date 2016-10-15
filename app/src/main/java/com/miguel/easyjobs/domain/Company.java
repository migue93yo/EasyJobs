package com.miguel.easyjobs.domain;

import java.io.Serializable;

public class Company implements Serializable {

    private String email;
    private String pass;
    private String name;
    private String description;
    private String province;

    public Company() {

    }

    public Company(String email, String name, String pass, String province) {
        this.email = email;
        this.name = name;
        this.pass = pass;
        this.province = province;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
