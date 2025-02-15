package com.dane.nkust_oom_backend.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.dane.nkust_oom_backend.service.NewsService;
import com.dane.nkust_oom_backend.dao.NewsDao;
import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import java.util.List;

@Component
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @Override
    public List<News> getNewsList() {
        return newsDao.getNewsList();
    }

    @Override
    public News getNewsById(Integer newsId) {
        return newsDao.getNewsById(newsId);
    }

    @Override
    public Integer createNews(NewsRequest newsRequest) {
        return newsDao.createNews(newsRequest);
    }

    @Override
    public void updateNews(Integer newsId, NewsRequest newsRequest) {
        newsDao.updateNews(newsId, newsRequest);
    }

    @Override
    public void deleteNewsById(Integer newsId) {
        newsDao.deleteNewsById(newsId);
    }
}
