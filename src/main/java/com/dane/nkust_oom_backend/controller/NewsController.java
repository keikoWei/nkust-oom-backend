package com.dane.nkust_oom_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import com.dane.nkust_oom_backend.model.News;
import com.dane.nkust_oom_backend.service.NewsService;
import com.dane.nkust_oom_backend.dto.NewsRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
public class NewsController {

    @Autowired
    private NewsService newsService;

    // 取得所有消息
    @GetMapping("/news")
    public ResponseEntity<List<News>> getNews() {

        List<News> newsList = newsService.getNewsList();
        
        return ResponseEntity.status(HttpStatus.OK).body(newsList);
    }

    // 取得單一消息
    @GetMapping("/news/{newsId}")
    public ResponseEntity<News> getNews(@PathVariable Integer newsId) {

        News news = newsService.getNewsById(newsId);

        if (news != null) {
            return ResponseEntity.status(HttpStatus.OK).body(news);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 新增消息
    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody @Valid NewsRequest newsRequest) {

        Integer newsId = newsService.createNews(newsRequest);

        News news = newsService.getNewsById(newsId);

        return ResponseEntity.status(HttpStatus.CREATED).body(news);
    }
    

    // 更新消息
    @PutMapping("/news/{newsId}")
    public ResponseEntity<News> updateNews(@PathVariable Integer newsId, @RequestBody @Valid NewsRequest newsRequest) {

        // 檢查消息是否存在
        News news = newsService.getNewsById(newsId);
        if (news == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 若存在，則更新消息
        newsService.updateNews(newsId, newsRequest);

        News updatedNews = newsService.getNewsById(newsId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedNews);
    }

    // 刪除消息
    @DeleteMapping("/news/{newsId}")
    public ResponseEntity<News> deleteNews(@PathVariable Integer newsId) {
        
        newsService.deleteNewsById(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
