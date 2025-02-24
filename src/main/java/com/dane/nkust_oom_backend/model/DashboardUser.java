package com.dane.nkust_oom_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DashboardUser {

    @JsonIgnore
    private Integer dashboardUserId;   // 後台使用者ID

    private String account;             // 登入帳號
    
    @JsonIgnore
    private String password;            // 登入密碼
    

    public Integer getDashboardUserId() {
        return dashboardUserId;
    }

    public void setDashboardUserId(Integer dashboardUserId) {
        this.dashboardUserId = dashboardUserId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

