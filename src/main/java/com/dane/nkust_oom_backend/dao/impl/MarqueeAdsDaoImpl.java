package com.dane.nkust_oom_backend.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.dane.nkust_oom_backend.dao.MarqueeAdsDao;
import com.dane.nkust_oom_backend.model.MarqueeAds;
import com.dane.nkust_oom_backend.rowmapper.MarqueeAdsRowMapper;
import com.dane.nkust_oom_backend.dto.MarqueeAdsRequest;
import org.springframework.stereotype.Component;


@Component
public class MarqueeAdsDaoImpl implements MarqueeAdsDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<MarqueeAds> getMarqueeAdsList() {

        String sql = 
        "SELECT AD_ID, IMAGE_URL, LINK_URL, DISPLAY_ORDER, ENABLE, PUBLISH_DATE, MODIFY_DATE " +
        "FROM marqueeads";

        Map<String, Object> map = new HashMap<>();

        List<MarqueeAds> marqueeAdsList = namedParameterJdbcTemplate.query(sql, map, new MarqueeAdsRowMapper());

        return marqueeAdsList;
    }

    @Override
    public MarqueeAds getMarqueeAdsById(Integer marqueeAdsId) {

        String sql = 
        "SELECT AD_ID, IMAGE_URL, LINK_URL, DISPLAY_ORDER, ENABLE, PUBLISH_DATE, MODIFY_DATE " +
        "FROM marqueeads " +
        "WHERE AD_ID = :adId";

        Map<String, Object> map = new HashMap<>();
        map.put("adId", marqueeAdsId);

        return namedParameterJdbcTemplate.queryForObject(sql, map, new MarqueeAdsRowMapper());
    }

    @Override
    public Integer createMarqueeAds(MarqueeAdsRequest marqueeAdsRequest) {

        String sql = 
        "INSERT INTO marqueeads (IMAGE_URL, LINK_URL, DISPLAY_ORDER, ENABLE, PUBLISH_DATE, MODIFY_DATE) " +
        "VALUES (:imageUrl, :linkUrl, :displayOrder, :enable, :publishDate, :modifyDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("imageUrl", marqueeAdsRequest.getImageUrl());
        map.put("linkUrl", marqueeAdsRequest.getLinkUrl());
        map.put("displayOrder", marqueeAdsRequest.getDisplayOrder());
        map.put("enable", marqueeAdsRequest.getEnable());
        map.put("publishDate", new Date());
        map.put("modifyDate", new Date());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int marqueeAdsId = keyHolder.getKey().intValue();

        return marqueeAdsId;    

    }

    @Override
    public void updateMarqueeAds(Integer marqueeAdsId, MarqueeAdsRequest marqueeAdsRequest) {

        String sql = 
        "UPDATE marqueeads SET IMAGE_URL = :imageUrl, LINK_URL = :linkUrl, DISPLAY_ORDER = :displayOrder, ENABLE = :enable, MODIFY_DATE = :modifyDate " +
        "WHERE AD_ID = :adId";

        Map<String, Object> map = new HashMap<>();
        map.put("imageUrl", marqueeAdsRequest.getImageUrl());
        map.put("linkUrl", marqueeAdsRequest.getLinkUrl());
        map.put("displayOrder", marqueeAdsRequest.getDisplayOrder());
        map.put("enable", marqueeAdsRequest.getEnable());
        map.put("modifyDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteMarqueeAds(Integer marqueeAdsId) {

        String sql = "DELETE FROM marqueeads WHERE AD_ID = :adId";
        Map<String, Object> map = new HashMap<>();
        map.put("adId", marqueeAdsId);
        namedParameterJdbcTemplate.update(sql, map);
    }   
}

