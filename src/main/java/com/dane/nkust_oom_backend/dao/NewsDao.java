package com.dane.nkust_oom_backend.dao;

import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.dto.NewsRequest;

public interface NewsDao {

    News getNewsById(Integer newsId);

    Integer createNews(NewsRequest newsRequest);

    void updateNews(Integer newsId, NewsRequest newsRequest);

    void deleteNewsById(Integer newsId);
}