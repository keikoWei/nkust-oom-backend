package com.dane.nkust_oom_backend.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.dane.nkust_oom_backend.model.News;
import org.springframework.lang.NonNull;
public class NewsRowMapper implements RowMapper<News> {

    @Override
    public News mapRow(@NonNull ResultSet resultSet, int i) throws SQLException {

        News news = new News();

        news.setNewsId(resultSet.getInt("NEWS_ID"));
        news.setCategoryId(resultSet.getInt("CATEGORY_ID"));
        news.setTitle(resultSet.getString("TITLE"));
        news.setContent(resultSet.getString("CONTENT"));
        news.setPublishDate(resultSet.getDate("PUBLISH_DATE"));
        news.setModifyDate(resultSet.getDate("MODIFY_DATE"));
        news.setAuthor(resultSet.getString("AUTHOR"));
        news.setEnable(resultSet.getBoolean("ENABLE"));
        news.setImageUrl(resultSet.getString("IMAGE_URL"));

        return news;
    }
}
