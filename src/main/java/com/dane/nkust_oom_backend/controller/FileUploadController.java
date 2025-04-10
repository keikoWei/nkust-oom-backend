package com.dane.nkust_oom_backend.controller;

import com.dane.nkust_oom_backend.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    
    @Autowired
    private FileService fileService;
    
    /**
     * 上傳文件 API
     * 支援 PDF 和 ODT 格式
     * 需要提供 newsId 參數關聯到新聞
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("newsId") Long newsId) {
        
        Map<String, Object> result = fileService.uploadFile(file, newsId);
        
        if (result != null && (boolean) result.getOrDefault("success", false)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 下載文件 API
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        Map<String, Object> fileData = fileService.downloadFile(filename);
        
        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            byte[] fileContent = (byte[]) fileData.get("content");
            String contentType = (String) fileData.get("contentType");
            
            // 對文件名進行 URL 編碼，處理特殊字符
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"")
                    .header("Content-Type", contentType)
                    .body(fileContent);
        } catch (Exception e) {
            logger.error("文件下載失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * 獲取文件列表 API
     */
    @GetMapping("/list")
    public ResponseEntity<?> listFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }
    
    /**
     * 根據新聞 ID 獲取文件列表
     */
    @GetMapping("/news/{newsId}")
    public ResponseEntity<?> getFilesByNewsId(@PathVariable Long newsId) {
        if (!fileService.isValidNewsId(newsId)) {
            logger.warn("無效的新聞 ID: {}", newsId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", "找不到指定的新聞"));
        }
        
        return ResponseEntity.ok(fileService.getFilesByNewsId(newsId));
    }
    
    /**
     * 刪除文件 API
     */
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer fileId) {
        boolean deleted = fileService.deleteFile(fileId);
        
        if (deleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "文件刪除成功"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 