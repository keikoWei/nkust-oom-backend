package com.dane.nkust_oom_backend.dto;

import com.dane.nkust_oom_backend.constant.NewsCategory;

public class NewsQueryParams {

    private NewsCategory category;
    private String search;

    public NewsCategory getCategory() {
        return category;
    }

    public void setCategory(NewsCategory category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
