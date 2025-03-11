package com.dane.nkust_oom_backend.service;

import com.dane.nkust_oom_backend.model.NewsFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * 文件服務接口
 * 處理文件上傳、下載、列表和刪除等操作
 */
public interface FileService {
    
    /**
     * 上傳文件
     * @param file 要上傳的文件
     * @param newsId 關聯的新聞 ID
     * @return 上傳結果，包含文件信息
     */
    Map<String, Object> uploadFile(MultipartFile file, Long newsId);
    
    /**
     * 下載文件
     * @param filename 文件名
     * @return 文件內容和類型
     */
    Map<String, Object> downloadFile(String filename);
    
    /**
     * 獲取所有文件列表
     * @return 文件列表
     */
    List<Map<String, Object>> getAllFiles();
    
    /**
     * 根據新聞 ID 獲取文件列表
     * @param newsId 新聞 ID
     * @return 文件列表
     */
    List<NewsFiles> getFilesByNewsId(Long newsId);
    
    /**
     * 刪除文件
     * @param fileId 文件 ID
     * @return 刪除結果
     */
    boolean deleteFile(Integer fileId);
    
    /**
     * 檢查文件類型是否支持
     * @param contentType 文件類型
     * @return 是否支持
     */
    boolean isSupportedFileType(String contentType);
    
    /**
     * 檢查新聞 ID 是否有效
     * @param newsId 新聞 ID
     * @return 是否有效
     */
    boolean isValidNewsId(Long newsId);
    
    /**
     * 檢查同一新聞下是否已存在相同名稱的文件
     * @param newsId 新聞 ID
     * @param fileName 文件名
     * @return 是否已存在
     */
    boolean isFileExistsForNews(Long newsId, String fileName);
    
    /**
     * 檢查文件系統中是否已存在相同名稱的文件
     * @param fileName 文件名
     * @return 是否已存在
     */
    boolean isFileExistsInFileSystem(String fileName);
} 