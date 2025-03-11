package com.dane.nkust_oom_backend.model;

import java.time.LocalDateTime;

/**
 * NewsFiles 實體類
 * 對應資料庫中的 NewsFiles 表
 */
public class NewsFiles {
    
    private Integer fileId;
    private Long newsId;
    private String fileName;
    private String fileUrl;
    private LocalDateTime uploadDate;
    
    /**
     * 默認構造函數
     */
    public NewsFiles() {
    }
    
    /**
     * 帶參數的構造函數
     * @param newsId 新聞 ID
     * @param fileName 文件名
     * @param fileUrl 文件 URL
     */
    public NewsFiles(Long newsId, String fileName, String fileUrl) {
        this.newsId = newsId;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.uploadDate = LocalDateTime.now();
    }
    
    /**
     * 完整參數的構造函數
     * @param fileId 文件 ID
     * @param newsId 新聞 ID
     * @param fileName 文件名
     * @param fileUrl 文件 URL
     * @param uploadDate 上傳時間
     */
    public NewsFiles(Integer fileId, Long newsId, String fileName, String fileUrl, LocalDateTime uploadDate) {
        this.fileId = fileId;
        this.newsId = newsId;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.uploadDate = uploadDate;
    }
    
    // Getters and Setters
    public Integer getFileId() {
        return fileId;
    }
    
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    
    public Long getNewsId() {
        return newsId;
    }
    
    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    @Override
    public String toString() {
        return "NewsFiles{" +
                "fileId=" + fileId +
                ", newsId=" + newsId +
                ", fileName='" + fileName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
} 