package com.dane.nkust_oom_backend.service;

import com.dane.nkust_oom_backend.dto.DashboardUserLoginRequest;
import com.dane.nkust_oom_backend.model.DashboardUser;

public interface DashboardUserService {
    
    DashboardUser login(DashboardUserLoginRequest dashboardUserLoginRequest);
}
