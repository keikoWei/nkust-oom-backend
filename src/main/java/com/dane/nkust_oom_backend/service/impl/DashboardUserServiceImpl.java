package com.dane.nkust_oom_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dane.nkust_oom_backend.dao.DashboardUserDao;
import com.dane.nkust_oom_backend.service.DashboardUserService;
import com.dane.nkust_oom_backend.dto.DashboardUserLoginRequest;
import com.dane.nkust_oom_backend.model.DashboardUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Component
public class DashboardUserServiceImpl implements DashboardUserService {

    @Autowired
    private DashboardUserDao dashboardUserDao;

    // log記錄
    private static final Logger log = LoggerFactory.getLogger(DashboardUserServiceImpl.class);

    @Override
    public DashboardUser login(DashboardUserLoginRequest dashboardUserLoginRequest) {

        DashboardUser dashboardUser = dashboardUserDao.getDashboardUserByAccount(dashboardUserLoginRequest.getAccount());

        //檢查帳號是否存在
        if (dashboardUser == null) {
            log.warn("帳號不存在: {}", dashboardUserLoginRequest.getAccount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "帳號不存在");
        }

        //檢查密碼是否正確
        if (!dashboardUser.getPassword().equals(dashboardUserLoginRequest.getPassword())) {
            log.warn("密碼錯誤: {}", dashboardUserLoginRequest.getAccount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼錯誤");
        }
        
        return dashboardUser;
    }
}
