package com.ritwik.rai.locatefactory;

import java.io.Serializable;

public class Company implements Serializable {


    private String companyID = "";

    private String companyPmsID = "";

    private String companyName = "";


    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyPmsID() {
        return companyPmsID;
    }

    public void setCompanyPmsID(String companyPmsID) {
        this.companyPmsID = companyPmsID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
