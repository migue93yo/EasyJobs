package com.miguel.easyjobs.domain;

import java.io.Serializable;
import java.util.Date;

public class Offer implements Serializable {

    private int id;
    private String email;
    private String title;
    private String province;
    private String job;
    private Date duration;
    private String contractType;
    private String workDay;
    private String description;
    private boolean state;

    public static final String CONTRACT_INDEFINITED = "Indefinido";
    public static final String CONTRACT_TEMPORAL = "Temporal";
    public static final String CONTRACT_IN_PRACTICES = "En prácticas";

    public static final String WORKDAY_COMPLETE = "Jornada completa";
    public static final String WORKDAY_HALF = "Media jornada";
    public static final String WORKDAY_PARTIAL = "Jornada parcial";

    public static final String STATE_SIGN_UP = "Inscrito";
    public static final String STATE_SELECTED = "Seleccionado";
    public static final String STATE_FINALIZED = "Finalizado";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
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
}
