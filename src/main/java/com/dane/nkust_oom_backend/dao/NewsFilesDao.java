package com.dane.nkust_oom_backend.dao;

import java.util.List;
import java.util.Optional;

import com.dane.nkust_oom_backend.model.NewsFiles;

/**
 * NewsFiles 資料訪問接口
 * 使用 JDBC 實現與 NewsFiles 表的互動
 */
public interface NewsFilesDao {
    
    /**
     * 保存文件資訊
     * @param newsFiles 文件資訊
     * @return 保存後的文件資訊（包含生成的 ID）
     */
    NewsFiles save(NewsFiles newsFiles);
    
    /**
     * 根據 ID 查詢文件
     * @param fileId 文件 ID
     * @return 文件資訊
     */
    Optional<NewsFiles> findById(Integer fileId);
    
    /**
     * 根據新聞 ID 查詢相關文件
     * @param newsId 新聞 ID
     * @return 文件列表
     */
    List<NewsFiles> findByNewsId(Long newsId);
    
    /**
     * 根據新聞 ID 和文件名查詢文件
     * @param newsId 新聞 ID
     * @param fileName 文件名
     * @return 文件資訊
     */
    Optional<NewsFiles> findByNewsIdAndFileName(Long newsId, String fileName);
    
    /**
     * 根據 ID 刪除文件
     * @param fileId 文件 ID
     * @return 是否刪除成功
     */
    boolean deleteById(Integer fileId);
    
    /**
     * 根據新聞 ID 刪除相關文件
     * @param newsId 新聞 ID
     * @return 刪除的記錄數
     */
    int deleteByNewsId(Long newsId);
} 