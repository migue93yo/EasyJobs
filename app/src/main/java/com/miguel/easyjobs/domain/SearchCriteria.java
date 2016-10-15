package com.miguel.easyjobs.domain;

import java.io.Serializable;

public class SearchCriteria implements Serializable {

    private String keyword;
    private String province;
    private String job;

    public SearchCriteria() {

    }

    public SearchCriteria(String keyword, String province, String job) {
        this.keyword = keyword;
        this.province = province;
        this.job = job;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
