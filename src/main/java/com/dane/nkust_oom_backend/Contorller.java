package com.dane.nkust_oom_backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Contorller {
    
    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
