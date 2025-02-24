package com.dane.nkust_oom_backend.dao;

import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import com.dane.nkust_oom_backend.dto.NewsQueryParams;
import java.util.List;

public interface NewsDao {

    List<News> getNewsList(NewsQueryParams newsQueryParams);

    News getNewsById(Long newsId);

    Long createNews(NewsRequest newsRequest);

    void updateNews(Long newsId, NewsRequest newsRequest);

    void deleteNewsById(Long newsId);
}