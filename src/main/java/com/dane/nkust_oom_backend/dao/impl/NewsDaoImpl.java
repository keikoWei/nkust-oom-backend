package com.dane.nkust_oom_backend.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.dane.nkust_oom_backend.dao.NewsDao;
import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.rowmapper.NewsRowMapper;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;


@Component
public class NewsDaoImpl implements NewsDao {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 生成唯一識別碼
    // DAO 層的私有方法
    private Long generateNewsId(Integer categoryId) {
        
        // 查詢當天該分類的最大序號
        String prefix = String.format("%02d", categoryId == null ? 0 : categoryId);
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String baseId = today + prefix;
        
        String sql = "SELECT MAX(NEWS_ID) FROM news " +
                    "WHERE NEWS_ID LIKE :prefix";
        
        Map<String, Object> params = new HashMap<>();
        params.put("prefix", baseId + "%");
        
        Long maxId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        
        // 如果當天該分類沒有消息，從001開始
        if (maxId == null) {
            return Long.parseLong(baseId + "001");
        } else {
            // 取出最後三位序號，加1後補零
            int sequence = (int)(maxId % 1000) + 1;
            return Long.parseLong(baseId + String.format("%03d", sequence));
        }
    }

    @Override
    public List<News> getNewsList() {

        String sql = 
        "SELECT NEWS_ID, CATEGORY_ID, TITLE, CONTENT, PUBLISH_DATE, MODIFY_DATE, AUTHOR, ENABLE, IMAGE_URL " +
        "FROM news";

        Map<String, Object> map = new HashMap<>();

        List<News> newsList = namedParameterJdbcTemplate.query(sql, map, new NewsRowMapper());

        return newsList;
    }
    
    @Override
     public News getNewsById(Long newsId) {
        
        String sql = 
        "SELECT NEWS_ID, CATEGORY_ID, TITLE, CONTENT, PUBLISH_DATE, MODIFY_DATE, AUTHOR, ENABLE, IMAGE_URL " +
        "FROM news " +
        "WHERE NEWS_ID = :newsId";

        Map<String, Object> map = new HashMap<>();
        map.put("newsId", newsId);

        List<News> newsList = namedParameterJdbcTemplate.query(sql, map, new NewsRowMapper());

        if (newsList.size() > 0) {
            return newsList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Long createNews(NewsRequest newsRequest) {

        // 生成唯一識別碼
        Long newsId = generateNewsId(newsRequest.getCategoryId());
        
        String sql = 
        "INSERT INTO news (NEWS_ID, CATEGORY_ID, TITLE, CONTENT, PUBLISH_DATE, MODIFY_DATE, AUTHOR, ENABLE, IMAGE_URL) " +
        "VALUES (:newsId, :categoryId, :title, :content, :publishDate, :modifyDate, :author, :enable, :imageUrl)";

        Map<String, Object> map = new HashMap<>();
        map.put("newsId", newsId);
        map.put("categoryId", newsRequest.getCategoryId());
        map.put("title", newsRequest.getTitle());
        map.put("content", newsRequest.getContent());
        map.put("author", newsRequest.getAuthor());
        map.put("enable", newsRequest.getEnable());
        map.put("imageUrl", newsRequest.getImageUrl());

        
        map.put("publishDate", new Date());
        map.put("modifyDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
        
        return newsId;
    }

    @Override
    public void updateNews(Long newsId, NewsRequest newsRequest) {
        
        String sql = 
        "UPDATE news SET CATEGORY_ID = :categoryId, TITLE = :title, CONTENT = :content, MODIFY_DATE = :modifyDate, AUTHOR = :author, ENABLE = :enable, IMAGE_URL = :imageUrl " +
        "WHERE NEWS_ID = :newsId";

        Map<String, Object> map = new HashMap<>();
        map.put("newsId", newsId);

        map.put("categoryId", newsRequest.getCategoryId());
        map.put("title", newsRequest.getTitle());
        map.put("content", newsRequest.getContent());
        map.put("author", newsRequest.getAuthor());
        map.put("enable", newsRequest.getEnable());
        map.put("imageUrl", newsRequest.getImageUrl());

        map.put("modifyDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteNewsById(Long newsId) {

        String sql = "DELETE FROM news WHERE NEWS_ID = :newsId";
        Map<String, Object> map = new HashMap<>();
        map.put("newsId", newsId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}