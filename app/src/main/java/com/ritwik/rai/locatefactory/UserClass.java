package com.ritwik.rai.locatefactory;

import java.io.Serializable;

/**
 * Created by ritwik on 8/8/2017.
 */

public class UserClass implements Serializable {

    // private components
    private String userName = "";
    private String userId = "";
    private String email = "";
    private String phone = "";
    private String adminID = "";
    private String password = "";

    // public components
    public boolean rememberMe = false;
    public boolean isLoggedin = false;
    public String deviceToken = "";
    public String selectedParentCompany = "";
    public Company selectedCompany;
    public Season selectedSeason;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
