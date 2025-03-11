package com.dane.nkust_oom_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;

@Configuration
@Component
public class FileStorageConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(FileStorageConfig.class);
    
    @Value("${file.upload.dir:uploads/files}")
    private String uploadDir;
    
    /**
     * 在專案啟動時創建上傳目錄
     */
    @PostConstruct
    public void init() {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    logger.info("成功創建文件上傳目錄: {}", directory.getAbsolutePath());
                } else {
                    logger.warn("無法創建文件上傳目錄: {}", directory.getAbsolutePath());
                }
            } else {
                logger.info("文件上傳目錄已存在: {}", directory.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("初始化文件上傳目錄時發生錯誤", e);
        }
    }
} 