package com.dane.nkust_oom_backend.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import com.dane.nkust_oom_backend.model.DashboardUser;

public class DashboardUserRowMapper implements RowMapper<DashboardUser> {

    @Override
    public DashboardUser mapRow(@NonNull ResultSet resultSet, int i) throws SQLException {
        
        DashboardUser dashboardUser = new DashboardUser();
        dashboardUser.setDashboardUserId(resultSet.getInt("DASHBOARD_USER_ID"));
        dashboardUser.setAccount(resultSet.getString("ACCOUNT"));
        dashboardUser.setPassword(resultSet.getString("PASSWORD"));

        return dashboardUser;
    }
}
