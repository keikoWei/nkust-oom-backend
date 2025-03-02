package com.dane.nkust_oom_backend.service.impl;

import com.dane.nkust_oom_backend.config.ImgurConfig;
import com.dane.nkust_oom_backend.model.ImgurResponse;
import com.dane.nkust_oom_backend.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;

@Service
public class ImgurServiceImpl implements ImgurService {

    private static final Logger logger = LoggerFactory.getLogger(ImgurServiceImpl.class);
    
    private final WebClient webClient;
    private final ImgurConfig imgurConfig;
    
    
    private final Map<String, Map<String, String>> userTokens = new ConcurrentHashMap<>();
    
    // 系統默認用戶 ID，用於存儲從配置文件中獲取的 token
    private static final String SYSTEM_TOKEN = "token";

    public ImgurServiceImpl(ImgurConfig imgurConfig) {
        this.imgurConfig = imgurConfig;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.imgur.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        boolean hasClientId = imgurConfig.getClientId() != null && !imgurConfig.getClientId().trim().isEmpty();
        boolean hasClientSecret = imgurConfig.getClientSecret() != null && !imgurConfig.getClientSecret().trim().isEmpty();
        boolean hasCallbackUrl = imgurConfig.getCallbackUrl() != null && !imgurConfig.getCallbackUrl().trim().isEmpty();
        boolean hasAccessToken = imgurConfig.getAccessToken() != null && !imgurConfig.getAccessToken().trim().isEmpty();
        boolean hasRefreshToken = imgurConfig.getRefreshToken() != null && !imgurConfig.getRefreshToken().trim().isEmpty();
        
        logger.info("ImgurServiceImpl 初始化完成");
        logger.info("配置檢查: " +
                "Client ID: {}, " +
                "Client Secret: {}, " +
                "Callback URL: {}, " +
                "Access Token: {}, " +
                "Refresh Token: {}", 
                hasClientId ? "已設定" : "未設定",
                hasClientSecret ? "已設定" : "未設定",
                hasCallbackUrl ? "已設定" : "未設定",
                hasAccessToken ? "已設定" : "未設定",
                hasRefreshToken ? "已設定" : "未設定");
        
        if (!hasAccessToken && !hasRefreshToken) {
            logger.warn("系統未配置 Imgur Access Token 或 Refresh Token，圖片上傳功能將無法正常工作");
        }
        
        if (hasAccessToken) {
            logger.info("已設定 Access Token，將在服務啟動時初始化系統 token");
        } else if (hasRefreshToken) {
            logger.info("未設定 Access Token 但已設定 Refresh Token，將嘗試在服務啟動時獲取新的 token");
        }
    }
    
    /**
     * 在服務啟動時自動執行，初始化系統 token
     */
    @PostConstruct
    @Override
    public void initializeSystemToken() {
        try {
            logger.info("開始初始化系統 Imgur token");
            
            String accessToken = imgurConfig.getAccessToken();
            String refreshToken = imgurConfig.getRefreshToken();
            
            boolean hasAccessToken = accessToken != null && !accessToken.trim().isEmpty();
            boolean hasRefreshToken = refreshToken != null && !refreshToken.trim().isEmpty();
            
            logger.info("配置檢查: Access token {}，Refresh token {}", 
                       hasAccessToken ? "已設定" : "未設定", 
                       hasRefreshToken ? "已設定" : "未設定");
            
            // 檢查現有系統 token 是否存在且有效
            Map<String, String> existingToken = getToken(SYSTEM_TOKEN);
            if (existingToken != null && existingToken.containsKey("access_token") && !testToken(existingToken.get("access_token"))) {
                logger.info("找到有效的系統 token，無需重新初始化");
                logger.debug("現有 token: {}..., 過期時間: {}",
                            existingToken.get("access_token").substring(0, Math.min(10, existingToken.get("access_token").length())),
                            existingToken.get("expires_in"));
                return;
            }
            
            // 如果沒有 access token 但有 refresh token
            if (!hasAccessToken && hasRefreshToken) {
                logger.info("沒有配置 access token，但找到 refresh token，將嘗試刷新");
                refreshAndStoreToken();
                return;
            }
            
            // 如果有 access token
            if (hasAccessToken) {
                logger.info("使用配置的 access token 初始化");
                
                // 創建 token 信息 Map
                Map<String, String> tokenInfo = new HashMap<>();
                tokenInfo.put("access_token", accessToken);
                if (hasRefreshToken) {
                    tokenInfo.put("refresh_token", refreshToken);
                }
                
                // 由於沒有過期時間信息，設置一個較長的預設過期時間（30天）
                tokenInfo.put("expires_in", "2592000");
                tokenInfo.put("created_at", String.valueOf(Instant.now().getEpochSecond()));
                tokenInfo.put("token_type", "bearer");
                
                // 將 token 存儲為系統 token
                storeToken(SYSTEM_TOKEN, tokenInfo);
                
                logger.info("系統 token 初始化成功");
                
                // 測試 token 是否有效
                try {
                    testToken(accessToken);
                } catch (Exception e) {
                    logger.warn("Token 測試失敗，但仍已存儲: {}", e.getMessage());
                }
                
                return;
            }
            
            // 既沒有 access token 也沒有 refresh token
            logger.error("配置文件中未找到 access token 或 refresh token，無法初始化系統 token");
            
        } catch (Exception e) {
            logger.error("初始化系統 token 失敗: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 使用 refresh token 刷新並存儲新的 token
     */
    private void refreshAndStoreToken() {
        try {
            String refreshToken = imgurConfig.getRefreshToken();
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                logger.error("無法刷新 token，refresh token 未配置或為空");
                return;
            }
            
            logger.info("開始使用 refresh token 獲取新的 access token");
            Map<String, String> tokenInfo = refreshAccessToken(refreshToken);
            
            if (tokenInfo != null && tokenInfo.containsKey("access_token")) {
                // 將獲取到的 token 存儲為系統 token
                storeToken(SYSTEM_TOKEN, tokenInfo);
                
                logger.info("成功刷新並存儲系統 token");
                
                // 可以進行 token 有效性測試
                try {
                    testToken(tokenInfo.get("access_token"));
                } catch (Exception e) {
                    logger.warn("新刷新的 token 測試失敗，但仍已存儲: {}", e.getMessage());
                }
            } else {
                logger.error("刷新 token 失敗，返回的 token 信息無效或不完整");
            }
        } catch (Exception e) {
            logger.error("刷新並存儲 token 失敗: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 測試 token 是否有效
     * @param accessToken 要測試的 access token
     * @return 如果 token 有效返回 true，否則返回 false
     */
    private boolean testToken(String accessToken) {
        try {
            logger.info("測試 token 有效性");
            
            if (accessToken == null || accessToken.isEmpty()) {
                logger.error("無法測試 token，token 為空");
                return false;
            }
            
            // 發送一個簡單請求測試 token 是否有效
            Map<String, Object> response = webClient.get()
                    .uri("/3/account/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            
            if (response != null && response.containsKey("success") && (boolean)response.get("success")) {
                // 獲取用戶名稱進行更詳細的日誌記錄
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                String username = data != null && data.containsKey("url") ? (String) data.get("url") : "unknown";
                
                logger.info("Token 有效性測試成功，確認為有效 token，用戶: {}", username);
                return true;
            } else {
                logger.warn("Token 有效性測試結果異常，回應不包含預期的成功標誌");
                if (response != null) {
                    logger.debug("回應內容: {}", response);
                }
                return false;
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                logger.error("Token 無效 (HTTP 401)，可能需要刷新: {}", e.getResponseBodyAsString());
                return false;
            } else if (e.getStatusCode().value() == 429) {
                logger.warn("測試 token 時遇到請求限制 (HTTP 429)，無法確定有效性，為安全起見視為有效");
                return true;
            } else {
                logger.error("測試 token 時發生 HTTP 錯誤: {}, 回應: {}", 
                          e.getStatusCode(), e.getResponseBodyAsString());
                return false;
            }
        } catch (Exception e) {
            logger.error("測試 token 時發生異常: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String uploadImage(MultipartFile file, String userId, String accessToken) {
        try {
            logger.info("開始使用 OAuth2 token 上傳圖片");
            
            // 檢查是否有有效的 token
            if (accessToken == null || accessToken.isEmpty()) {
                logger.error("缺少 OAuth2 token，無法上傳圖片");
                throw new RuntimeException("缺少 OAuth2 token，無法上傳圖片");
            }
            
            // 對於系統 TOKEN，檢查是否需要刷新
            if (SYSTEM_TOKEN.equals(userId)) {
                Map<String, String> tokenInfo = getToken(userId);
                if (tokenInfo != null && !testToken(tokenInfo.get("access_token"))) {
                    logger.info("系統 access token 已過期，嘗試自動刷新");
                    
                    String refreshToken = tokenInfo.get("refresh_token");
                    if (refreshToken != null && !refreshToken.isEmpty()) {
                        try {
                            // 嘗試刷新系統 token
                            refreshAndStoreToken();
                            // 獲取刷新後的 token
                            Map<String, String> newTokenInfo = getToken(SYSTEM_TOKEN);
                            if (newTokenInfo != null && newTokenInfo.containsKey("access_token")) {
                                accessToken = newTokenInfo.get("access_token");
                                logger.info("成功刷新系統 access token，使用新 token 繼續上傳");
                            } else {
                                logger.error("系統 token 刷新似乎成功但無法獲取新 token");
                            }
                        } catch (Exception e) {
                            logger.error("刷新系統 token 失敗", e);
                            throw new RuntimeException("系統授權已過期且無法自動刷新，請聯絡管理員", e);
                        }
                    } else {
                        logger.error("系統沒有可用的 refresh token，無法刷新 token");
                        throw new RuntimeException("系統授權已過期且無法自動刷新，請聯絡管理員");
                    }
                }
            }
            
            // 將圖片編碼為 Base64
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            
            logger.debug("使用 Bearer Token 上傳圖片");
            
            // 發送上傳請求
            ImgurResponse response = webClient.post()
                    .uri("/3/image")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(Map.of("image", base64Image))
                    .retrieve()
                    .bodyToMono(ImgurResponse.class)
                    .doOnError(e -> {
                        if (e instanceof WebClientResponseException) {
                            WebClientResponseException wcre = (WebClientResponseException) e;
                            logger.error("上傳圖片失敗: HTTP {}, 錯誤內容: {}", 
                                    wcre.getStatusCode(), wcre.getResponseBodyAsString());
                        } else {
                            logger.error("上傳圖片失敗", e);
                        }
                    })
                    .block();

            // 處理回應
            if (response != null && response.isSuccess()) {
                logger.info("圖片上傳成功, URL: {}", response.getData().getLink());
                return response.getData().getLink();
            } else {
                if (response != null) {
                    logger.error("上傳失敗, 回應: {}", response);
                } else {
                    logger.error("上傳失敗, 回應為空");
                }
                throw new RuntimeException("上傳圖片到 Imgur 失敗");
            }
        } catch (WebClientResponseException e) {
            logger.error("上傳圖片失敗: HTTP {}, 錯誤內容: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            
            // 根據錯誤類型提供更具體的錯誤訊息
            if (e.getStatusCode().value() == 401) {
                throw new RuntimeException("上傳圖片到 Imgur 失敗: 授權無效，請刷新系統 token", e);
            } else if (e.getStatusCode().value() == 429) {
                throw new RuntimeException("上傳圖片到 Imgur 失敗: 超過 API 請求限制，請稍後再試", e);
            } else {
                throw new RuntimeException("上傳圖片到 Imgur 失敗: " + e.getResponseBodyAsString(), e);
            }
        } catch (Exception e) {
            logger.error("上傳圖片過程中發生錯誤", e);
            throw new RuntimeException("上傳圖片到 Imgur 失敗: " + e.getMessage(), e);
        }
    }

    
    @Override
    public Map<String, String> refreshAccessToken(String refreshToken) {
        try {
            logger.info("開始使用 refresh token 刷新 access token");
            
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new IllegalArgumentException("Refresh token 不能為空");
            }
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", imgurConfig.getClientId());
            formData.add("client_secret", imgurConfig.getClientSecret());
            formData.add("grant_type", "refresh_token");
            formData.add("refresh_token", refreshToken);

            logger.debug("刷新 token 請求參數: {}", formData);
            
            Map<String, Object> response = webClient.post()
                    .uri("https://api.imgur.com/oauth2/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .doOnError(e -> {
                        if (e instanceof WebClientResponseException) {
                            WebClientResponseException wcre = (WebClientResponseException) e;
                            logger.error("刷新 token 失敗: HTTP {}, 錯誤內容: {}", 
                                    wcre.getStatusCode(), wcre.getResponseBodyAsString());
                        } else {
                            logger.error("刷新 token 失敗", e);
                        }
                    })
                    .block();

            logger.debug("刷新 token 回應: {}", response);
            
            Map<String, String> result = new HashMap<>();
            if (response != null && response.containsKey("access_token")) {
                result.put("access_token", (String) response.get("access_token"));
                
                // Imgur 在刷新時也會提供新的 refresh_token
                if (response.containsKey("refresh_token")) {
                    result.put("refresh_token", (String) response.get("refresh_token"));
                } else {
                    // 如果沒有提供新的 refresh_token，則使用原來的
                    result.put("refresh_token", refreshToken);
                }
                
                result.put("expires_in", String.valueOf(response.get("expires_in")));
                result.put("token_type", (String) response.get("token_type"));
                
                // 添加 token 獲取時間戳
                result.put("created_at", String.valueOf(Instant.now().getEpochSecond()));
                
                logger.info("成功刷新 access token");
                return result;
            } else {
                logger.error("刷新 token 回應中沒有 access_token: {}", response);
                throw new RuntimeException("無法刷新 Imgur 訪問令牌: 回應無效");
            }
        } catch (WebClientResponseException e) {
            logger.error("刷新 token 失敗: HTTP {}, 錯誤內容: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("刷新 Imgur 訪問令牌失敗: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("刷新 token 時發生異常", e);
            throw new RuntimeException("刷新 Imgur 訪問令牌失敗: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Map<String, String> getToken(String userId) {
        return userTokens.get(userId);
    }
    
    @Override
    public void storeToken(String userId, Map<String, String> tokenInfo) {
        userTokens.put(userId, tokenInfo);
    }
} 