package com.dane.nkust_oom_backend.dao;

import java.util.List;
import com.dane.nkust_oom_backend.model.MarqueeAds;
import com.dane.nkust_oom_backend.dto.MarqueeAdsRequest;

public interface MarqueeAdsDao {

    List<MarqueeAds> getMarqueeAdsList();

    MarqueeAds getMarqueeAdsById(Integer marqueeAdsId);

    Integer createMarqueeAds(MarqueeAdsRequest marqueeAdsRequest);

    void updateMarqueeAds(Integer marqueeAdsId, MarqueeAdsRequest marqueeAdsRequest);
    
    void deleteMarqueeAds(Integer marqueeAdsId);
    
}
