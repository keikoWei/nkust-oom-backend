package com.dane.nkust_oom_backend.service;

import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import java.util.List;

public interface NewsService {

    List<News> getNewsList();

    News getNewsById(Long newsId);

    Long createNews(NewsRequest newsRequest);

    void updateNews(Long newsId, NewsRequest newsRequest);

    void deleteNewsById(Long newsId);
    
}
