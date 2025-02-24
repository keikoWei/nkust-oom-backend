package com.dane.nkust_oom_backend.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.dane.nkust_oom_backend.dao.DashboardUserDao;
import com.dane.nkust_oom_backend.model.DashboardUser;
import com.dane.nkust_oom_backend.rowmapper.DashboardUserRowMapper;

@Component
public class DashboardUserDaoImpl implements DashboardUserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public DashboardUser getDashboardUserByAccount(String account) {

        String sql = "SELECT DASHBOARD_USER_ID, ACCOUNT, PASSWORD FROM dashboarduser WHERE ACCOUNT = :account";
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<DashboardUser> dashboardUserList = namedParameterJdbcTemplate.query(sql, map, new DashboardUserRowMapper());
    
        if (dashboardUserList.size() > 0) {
            return dashboardUserList.get(0);
        } else {
            return null;
        }
    }
}