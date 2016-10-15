package com.miguel.easyjobs.domain;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {

    private String email;
    private String pass;
    private String fullname;
    private String province;
    private Date birthdate;
    private String phone;

    public Employee() {

    }

    public Employee(String email, String pass, String fullname, String province, Date birthdate, String phone) {
        this.email = email;
        this.pass = pass;
        this.fullname = fullname;
        this.province = province;
        this.birthdate = birthdate;
        this.phone = phone;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
