package com.dane.nkust_oom_backend.service;

import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.dto.NewsRequest;

public interface NewsService {

    News getNewsById(Integer newsId);

    Integer createNews(NewsRequest newsRequest);

    void updateNews(Integer newsId, NewsRequest newsRequest);

    void deleteNewsById(Integer newsId);
    
}
