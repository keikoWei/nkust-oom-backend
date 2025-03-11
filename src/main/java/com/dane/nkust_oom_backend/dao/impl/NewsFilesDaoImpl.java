package com.dane.nkust_oom_backend.dao.impl;

import com.dane.nkust_oom_backend.dao.NewsFilesDao;
import com.dane.nkust_oom_backend.model.NewsFiles;
import com.dane.nkust_oom_backend.rowmapper.NewsFilesRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Repository
public class NewsFilesDaoImpl implements NewsFilesDao {

    
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private NewsFilesRowMapper newsFilesRowMapper;
    
    @Override
    public NewsFiles save(NewsFiles newsFiles) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            String sql = "INSERT INTO NewsFiles (NEWS_ID, FILE_NAME, FILE_URL, UPLOAD_DATE) " +
                         "VALUES (:newsId, :fileName, :fileUrl, :uploadDate)";
            
            LocalDateTime uploadDate = newsFiles.getUploadDate();
            if (uploadDate == null) {
                uploadDate = LocalDateTime.now();
            }
            
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("newsId", newsFiles.getNewsId())
                .addValue("fileName", newsFiles.getFileName())
                .addValue("fileUrl", newsFiles.getFileUrl())
                .addValue("uploadDate", Timestamp.valueOf(uploadDate));
            
            namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"FILE_ID"});
            
            Number key = keyHolder.getKey();
            if (key != null) {
                newsFiles.setFileId(key.intValue());
            }
            
            return newsFiles;
        } catch (DataAccessException e) {
            throw e;
        }
    }
    
    @Override
    public Optional<NewsFiles> findById(Integer fileId) {
        try {
            String sql = "SELECT * FROM NewsFiles WHERE FILE_ID = :fileId";
            
            Map<String, Object> params = new HashMap<>();
            params.put("fileId", fileId);
            
            try {
                NewsFiles newsFiles = namedParameterJdbcTemplate.queryForObject(
                        sql, params, newsFilesRowMapper);
                return Optional.ofNullable(newsFiles);
            } catch (EmptyResultDataAccessException e) {
                
                return Optional.empty();
            }
        } catch (DataAccessException e) {
            
            throw e;
        }
    }
    
    @Override
    public List<NewsFiles> findByNewsId(Long newsId) {
        try {
            String sql = "SELECT * FROM NewsFiles WHERE NEWS_ID = :newsId";
            
            Map<String, Object> params = new HashMap<>();
            params.put("newsId", newsId);
            
            List<NewsFiles> newsFiles = namedParameterJdbcTemplate.query(
                    sql, params, newsFilesRowMapper);
            
            
            return newsFiles;
        } catch (DataAccessException e) {
           
            throw e;
        }
    }
    
    @Override
    public Optional<NewsFiles> findByNewsIdAndFileName(Long newsId, String fileName) {
        try {
            String sql = "SELECT * FROM NewsFiles WHERE NEWS_ID = :newsId AND FILE_NAME = :fileName";
            
            Map<String, Object> params = new HashMap<>();
            params.put("newsId", newsId);
            params.put("fileName", fileName);
            
            try {
                NewsFiles newsFiles = namedParameterJdbcTemplate.queryForObject(
                        sql, params, newsFilesRowMapper);
                return Optional.ofNullable(newsFiles);
            } catch (EmptyResultDataAccessException e) {
                
                return Optional.empty();
            }
        } catch (DataAccessException e) {
            
            throw e;
        }
    }
    
    @Override
    public boolean deleteById(Integer fileId) {
        try {
            String sql = "DELETE FROM NewsFiles WHERE FILE_ID = :fileId";
            
            Map<String, Object> params = new HashMap<>();
            params.put("fileId", fileId);
            
            int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
            
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            
            throw e;
        }
    }
    
    @Override
    public int deleteByNewsId(Long newsId) {
        try {
            String sql = "DELETE FROM NewsFiles WHERE NEWS_ID = :newsId";
            
            Map<String, Object> params = new HashMap<>();
            params.put("newsId", newsId);
            
            int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
            
            return rowsAffected;
        } catch (DataAccessException e) {
            
            throw e;
        }
    }
} 