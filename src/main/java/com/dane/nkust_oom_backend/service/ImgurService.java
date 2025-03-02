package com.dane.nkust_oom_backend.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * Imgur 圖片上傳服務
 */
public interface ImgurService {
    
    /**
     * 用於存儲系統初始化的 token 
     */
    String SYSTEM_TOKEN = "token";
    
    /**
     * 使用 OAuth2 token 上傳圖片
     * @param file 要上傳的圖片
     * @param userId 用戶 ID
     * @param accessToken OAuth2 token
     * @return 上傳成功後的圖片 URL
     */
    String uploadImage(MultipartFile file, String userId, String accessToken);
    
    /**
     * 使用 refresh token 獲取新的 access token
     * @param refreshToken 用於刷新的 refresh token
     * @return 新的 token 信息，包含新的 access_token 和 refresh_token
     */
    Map<String, String> refreshAccessToken(String refreshToken);
    
    /**
     * 獲取token 信息
     */
    Map<String, String> getToken(String userId);
    
    /**
     * 存儲token 信息
     */
    void storeToken(String userId, Map<String, String> tokenInfo);
    
    /**
     * 初始化系統 token
     * 使用配置文件中的 access token 和 refresh token
     */
    void initializeSystemToken();
} 