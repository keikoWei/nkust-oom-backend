package com.dane.nkust_oom_backend.rowmapper;

import com.dane.nkust_oom_backend.model.NewsFiles;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class NewsFilesRowMapper implements RowMapper<NewsFiles> {

    @Override
    public NewsFiles mapRow(@NonNull ResultSet rs, int i) throws SQLException {
        NewsFiles newsFiles = new NewsFiles();
        
        newsFiles.setFileId(rs.getInt("FILE_ID"));
        newsFiles.setNewsId(rs.getLong("NEWS_ID"));
        newsFiles.setFileName(rs.getString("FILE_NAME"));
        newsFiles.setFileUrl(rs.getString("FILE_URL"));
        
        Timestamp uploadDate = rs.getTimestamp("UPLOAD_DATE");
        if (uploadDate != null) {
            newsFiles.setUploadDate(uploadDate.toLocalDateTime());
        }
        
        return newsFiles;
    }
} 