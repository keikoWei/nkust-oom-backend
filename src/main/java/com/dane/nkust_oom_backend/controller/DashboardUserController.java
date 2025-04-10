package com.dane.nkust_oom_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.dane.nkust_oom_backend.service.DashboardUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.dane.nkust_oom_backend.model.DashboardUser;
import com.dane.nkust_oom_backend.dto.DashboardUserLoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class DashboardUserController {

    @Autowired
    private DashboardUserService dashboardUserService;

    
    @PostMapping("/dashboardUsers/login")
    public ResponseEntity<DashboardUser> login(@RequestBody @Valid DashboardUserLoginRequest dashboardUserLoginRequest) {
        
        DashboardUser dashboardUser = dashboardUserService.login(dashboardUserLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(dashboardUser);
    }
}