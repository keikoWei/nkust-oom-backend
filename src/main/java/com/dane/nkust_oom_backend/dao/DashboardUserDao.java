package com.dane.nkust_oom_backend.dao;

import com.dane.nkust_oom_backend.model.DashboardUser;

public interface DashboardUserDao {
    
    DashboardUser getDashboardUserByAccount(String account);
}
