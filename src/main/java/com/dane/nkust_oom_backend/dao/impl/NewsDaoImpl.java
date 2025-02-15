package com.dane.nkust_oom_backend.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.dane.nkust_oom_backend.dao.NewsDao;
import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.rowmapper.NewsRowMapper;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;


@Component
public class NewsDaoImpl implements NewsDao {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
     public News getNewsById(Integer newsId) {
        
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
    public Integer createNews(NewsRequest newsRequest) {

        String sql = 
        "INSERT INTO news (CATEGORY_ID, TITLE, CONTENT, PUBLISH_DATE, MODIFY_DATE, AUTHOR, ENABLE, IMAGE_URL) " +
        "VALUES (:categoryId, :title, :content, :publishDate, :modifyDate, :author, :enable, :imageUrl)";

        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", newsRequest.getCategoryId());
        map.put("title", newsRequest.getTitle());
        map.put("content", newsRequest.getContent());
        map.put("author", newsRequest.getAuthor());
        map.put("enable", newsRequest.getEnable());
        map.put("imageUrl", newsRequest.getImageUrl());

        
        map.put("publishDate", new Date());
        map.put("modifyDate", new Date());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int newsId = keyHolder.getKey().intValue();

        return newsId;
    }

    @Override
    public void updateNews(Integer newsId, NewsRequest newsRequest) {
        
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
    public void deleteNewsById(Integer newsId) {

        String sql = "DELETE FROM news WHERE NEWS_ID = :newsId";
        Map<String, Object> map = new HashMap<>();
        map.put("newsId", newsId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}