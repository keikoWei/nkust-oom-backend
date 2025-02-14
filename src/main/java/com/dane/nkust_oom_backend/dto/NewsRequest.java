package com.dane.nkust_oom_backend.dto;

import jakarta.validation.constraints.NotNull;

public class NewsRequest {
    
    @NotNull
    private Integer categoryId;     // 類別

    @NotNull
    private String title;           // 標題

    @NotNull
    private String content;         // 內容

    @NotNull
    private String author;          // 作者

    private Boolean enable;         // 是否啟用
    private String imageUrl;        // 圖片URL

    // Getter 和 Setter 方法

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}