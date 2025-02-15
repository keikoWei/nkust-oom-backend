package com.dane.nkust_oom_backend.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.dane.nkust_oom_backend.model.MarqueeAds;
import org.springframework.lang.NonNull;

public class MarqueeAdsRowMapper implements RowMapper<MarqueeAds> {

    @Override
    public MarqueeAds mapRow(@NonNull ResultSet resultSet, int i) throws SQLException {

        MarqueeAds marqueeAds = new MarqueeAds();

        marqueeAds.setAdId(resultSet.getInt("AD_ID"));
        marqueeAds.setImageUrl(resultSet.getString("IMAGE_URL"));
        marqueeAds.setLinkUrl(resultSet.getString("LINK_URL"));
        marqueeAds.setDisplayOrder(resultSet.getInt("DISPLAY_ORDER"));
        marqueeAds.setEnable(resultSet.getBoolean("ENABLE"));
        marqueeAds.setPublishDate(resultSet.getDate("PUBLISH_DATE"));
        marqueeAds.setModifyDate(resultSet.getDate("MODIFY_DATE"));

        return marqueeAds;
    }
}
