package com.dane.nkust_oom_backend.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.dane.nkust_oom_backend.service.NewsService;
import com.dane.nkust_oom_backend.dao.NewsDao;
import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import com.dane.nkust_oom_backend.dto.NewsQueryParams;
import java.util.List;
import java.lang.Long;

@Component
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @Override
    public List<News> getNewsList(NewsQueryParams newsQueryParams) {
        return newsDao.getNewsList(newsQueryParams);
    }

    @Override
    public News getNewsById(Long newsId) {
        return newsDao.getNewsById(newsId);
    }

    @Override
    public Long createNews(NewsRequest newsRequest) {
        return newsDao.createNews(newsRequest);
    }

    @Override
    public void updateNews(Long newsId, NewsRequest newsRequest) {
        newsDao.updateNews(newsId, newsRequest);
    }

    @Override
    public void deleteNewsById(Long newsId) {
        newsDao.deleteNewsById(newsId);
    }
}
