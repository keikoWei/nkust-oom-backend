package com.dane.nkust_oom_backend.controller;

import com.dane.nkust_oom_backend.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/imgur")
public class ImgurController {

    private static final Logger logger = LoggerFactory.getLogger(ImgurController.class);
    
    @Autowired
    private ImgurService imgurService;

    /**
     * 整合式圖片上傳 API，統一使用系統 OAuth2 token
     * 所有上傳都使用系統的 token
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("請選擇要上傳的圖片");
            }
            
            // 檢查系統是否有初始化的 token
            Map<String, String> systemTokenInfo = imgurService.getToken(ImgurService.SYSTEM_TOKEN);
            if (systemTokenInfo != null && systemTokenInfo.containsKey("access_token")) {
                logger.info("使用系統 token 進行上傳");
                String systemToken = systemTokenInfo.get("access_token");
                String imageUrl = imgurService.uploadImage(file, ImgurService.SYSTEM_TOKEN, systemToken);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "url", imageUrl
                ));
            }
            // 如果系統沒有 token，返回錯誤
            else {
                logger.error("系統未初始化 OAuth2 token，無法上傳圖片");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of(
                            "success", false,
                            "error", "系統尚未設定 Imgur 授權，請聯絡管理員設定"
                        ));
            }
        } catch (Exception e) {
            logger.error("上傳圖片失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", e.getMessage()
                    ));
        }
    }
    
    /**
     * 檢查系統 token 狀態
     * 用於調試和檢查 token 信息
     */
    @GetMapping("/token/status")
    public ResponseEntity<?> checkTokenStatus() {
        try {
            // 獲取系統 token
            Map<String, String> systemTokenInfo = imgurService.getToken(ImgurService.SYSTEM_TOKEN);
            
            if (systemTokenInfo == null || !systemTokenInfo.containsKey("access_token")) {
                return ResponseEntity.ok(Map.of(
                    "status", "unavailable",
                    "message", "系統尚未設定 Imgur 授權，請使用 refresh 端點初始化 token"
                ));
            }
            
            return ResponseEntity.ok(Map.of(
                "status", "valid",
                "access_token", systemTokenInfo.get("access_token").substring(0, 10) + "...", // 只顯示部分 token，保護安全
                "token_type", systemTokenInfo.getOrDefault("token_type", "unknown"),
                "expires_in", systemTokenInfo.getOrDefault("expires_in", "0"),
                "created_at", systemTokenInfo.getOrDefault("created_at", "0"),
                "has_refresh_token", systemTokenInfo.containsKey("refresh_token")
            ));
        } catch (Exception e) {
            logger.error("檢查 token 狀態失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", "檢查 token 狀態失敗: " + e.getMessage()
                    ));
        }
    }
    
    /**
     * 手動刷新系統 token
     * 用於調試和維護
     */
    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshSystemToken() {
        try {
            logger.info("開始手動刷新系統 token");
            imgurService.initializeSystemToken();
            
            // 獲取刷新後的系統 token 信息
            Map<String, String> tokenInfo = imgurService.getToken(ImgurService.SYSTEM_TOKEN);
            
            if (tokenInfo != null && tokenInfo.containsKey("access_token")) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "系統 token 刷新成功",
                    "access_token", tokenInfo.get("access_token").substring(0, 10) + "...", // 只顯示部分 token，保護安全
                    "token_type", tokenInfo.getOrDefault("token_type", "unknown"),
                    "expires_in", tokenInfo.getOrDefault("expires_in", "0"),
                    "created_at", tokenInfo.getOrDefault("created_at", "0"),
                    "has_refresh_token", tokenInfo.containsKey("refresh_token")
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", "無法刷新系統 token，可能是配置問題或 refresh token 已失效"
                    ));
            }
        } catch (Exception e) {
            logger.error("刷新系統 token 失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", "刷新系統 token 失敗: " + e.getMessage()
                    ));
        }
    }
} 