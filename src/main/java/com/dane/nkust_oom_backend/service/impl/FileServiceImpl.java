package com.dane.nkust_oom_backend.service.impl;

import com.dane.nkust_oom_backend.dao.NewsFilesDao;
import com.dane.nkust_oom_backend.dao.NewsDao;
import com.dane.nkust_oom_backend.model.NewsFiles;
import com.dane.nkust_oom_backend.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    
    // 支援的文件類型
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(
            "application/pdf",                     // PDF
            "application/vnd.oasis.opendocument.text" // ODT
    );
    
    // 文件上傳目錄
    @Value("${file.upload.dir:uploads}")
    private String uploadDir;
    
    @Autowired
    private NewsFilesDao newsFilesDao;
    
    @Autowired
    private NewsDao newsDao;
    
    @Override
    public Map<String, Object> uploadFile(MultipartFile file, Long newsId) {
        try {
            // 檢查 newsId 是否有效
            if (!isValidNewsId(newsId)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "無效的新聞 ID");
                return response;
            }
            
            // 檢查文件是否為空
            if (file.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "請選擇要上傳的文件");
                return response;
            }
            
            // 檢查文件類型
            String contentType = file.getContentType();
            if (!isSupportedFileType(contentType)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "不支援的文件格式，僅支援 PDF 和 ODT 格式");
                return response;
            }
            
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "無效的文件名稱");
                return response;
            }
            
            // 檢查同一新聞下是否已存在相同名稱的文件
            if (isFileExistsForNews(newsId, originalFilename)) {
                logger.warn("文件上傳失敗：新聞 ID {} 下已存在文件名稱 '{}'", newsId, originalFilename);
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "上傳失敗，該新聞下已有相同的檔案名稱: " + originalFilename);
                return response;
            }
            
            // 檢查文件系統中是否已存在相同名稱的文件
            if (isFileExistsInFileSystem(originalFilename)) {
                logger.warn("文件上傳失敗：文件名稱 '{}' 已存在於文件系統中", originalFilename);
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "上傳失敗，已有相同的檔案名稱: " + originalFilename);
                return response;
            }
            
            // 保存文件
            Path filePath = Paths.get(uploadDir, originalFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // 構建文件 URL
            String fileUrl = "/files/download/" + originalFilename;
            
            // 保存文件資訊到資料庫
            NewsFiles newsFile = new NewsFiles(newsId, originalFilename, fileUrl);
            newsFile = newsFilesDao.save(newsFile);
            
            logger.info("文件上傳成功: {}, 關聯新聞 ID: {}", filePath, newsId);
            
            // 返回成功回應
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileId", newsFile.getFileId());
            response.put("newsId", newsId);
            response.put("filename", originalFilename);
            response.put("contentType", contentType);
            response.put("path", fileUrl);
            response.put("uploadDate", newsFile.getUploadDate());
            return response;
            
        } catch (Exception e) {
            logger.error("文件上傳失敗", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
    
    @Override
    public Map<String, Object> downloadFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir, filename);
            if (!Files.exists(filePath)) {
                return null; // 文件不存在
            }
            
            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            
            Map<String, Object> result = new HashMap<>();
            result.put("content", fileContent);
            result.put("contentType", contentType != null ? contentType : "application/octet-stream");
            result.put("filename", filename);
            
            return result;
        } catch (Exception e) {
            logger.error("文件下載失敗", e);
            return null;
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllFiles() {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists() || !dir.isDirectory()) {
                return new ArrayList<>();
            }
            
            File[] files = dir.listFiles();
            if (files == null) {
                return new ArrayList<>();
            }
            
            List<Map<String, Object>> fileList = new ArrayList<>();
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        String contentType = Files.probeContentType(file.toPath());
                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("filename", file.getName());
                        fileInfo.put("contentType", contentType != null ? contentType : "unknown");
                        fileInfo.put("lastModified", file.lastModified());
                        fileInfo.put("path", "/files/download/" + file.getName());
                        fileList.add(fileInfo);
                    } catch (IOException e) {
                        logger.error("獲取文件信息失敗: {}", file.getName(), e);
                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("filename", file.getName());
                        fileInfo.put("contentType", "unknown");
                        fileInfo.put("lastModified", file.lastModified());
                        fileInfo.put("path", "/files/download/" + file.getName());
                        fileList.add(fileInfo);
                    }
                }
            }
            
            return fileList;
        } catch (Exception e) {
            logger.error("獲取文件列表失敗", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<NewsFiles> getFilesByNewsId(Long newsId) {
        try {
            // 檢查新聞 ID 是否有效
            if (!isValidNewsId(newsId)) {
                logger.warn("嘗試獲取無效新聞 ID 的文件列表: {}", newsId);
                return new ArrayList<>();
            }
            
            return newsFilesDao.findByNewsId(newsId);
        } catch (Exception e) {
            logger.error("獲取新聞文件列表失敗", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean deleteFile(Integer fileId) {
        try {
            // 從資料庫中查詢文件資訊
            Optional<NewsFiles> optionalNewsFile = newsFilesDao.findById(fileId);
            
            if (optionalNewsFile.isEmpty()) {
                return false;
            }
            
            NewsFiles newsFile = optionalNewsFile.get();
            
            // 刪除實際文件
            Path filePath = Paths.get(uploadDir, newsFile.getFileName());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            // 從資料庫中刪除記錄
            boolean deleted = newsFilesDao.deleteById(fileId);
            
            if (deleted) {
                logger.info("文件刪除成功: {}, 文件 ID: {}", newsFile.getFileName(), fileId);
            } else {
                logger.error("刪除文件記錄失敗, ID: {}", fileId);
            }
            
            return deleted;
        } catch (Exception e) {
            logger.error("文件刪除失敗", e);
            return false;
        }
    }
    
    @Override
    public boolean isSupportedFileType(String contentType) {
        return contentType != null && SUPPORTED_FORMATS.contains(contentType);
    }
    
    @Override
    public boolean isValidNewsId(Long newsId) {
        if (newsId == null || newsId <= 0) {
            return false;
        }
        
        try {
            // 檢查資料庫中是否存在對應的新聞記錄
            return newsDao.getNewsById(newsId) != null;
        } catch (Exception e) {
            logger.error("檢查新聞 ID 是否有效時發生錯誤: {}", newsId, e);
            return false;
        }
    }
    
    @Override
    public boolean isFileExistsForNews(Long newsId, String fileName) {
        Optional<NewsFiles> existingFile = newsFilesDao.findByNewsIdAndFileName(newsId, fileName);
        return existingFile.isPresent();
    }
    
    @Override
    public boolean isFileExistsInFileSystem(String fileName) {
        Path filePath = Paths.get(uploadDir, fileName);
        return Files.exists(filePath);
    }
} 