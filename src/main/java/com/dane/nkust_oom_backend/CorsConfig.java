package com.dane.nkust_oom_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("https://oom.nkust.edu.tw"); // 正式環境域名
        config.addAllowedOrigin("http://localhost"); // 允許的前端域名
        config.addAllowedMethod("*"); // 允許的請求方法 (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*"); // 允許的請求頭
        config.setAllowCredentials(true); // 是否允許攜帶 Cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 對所有路徑生效

        return new CorsFilter(source);
    }
    
}
