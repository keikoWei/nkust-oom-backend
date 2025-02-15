package com.dane.nkust_oom_backend.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.dane.nkust_oom_backend.dao.MarqueeAdsDao;
import com.dane.nkust_oom_backend.model.MarqueeAds;
import com.dane.nkust_oom_backend.service.MarqueeAdsService;
import com.dane.nkust_oom_backend.dto.MarqueeAdsRequest;
import org.springframework.stereotype.Component;

@Component
public class MarqueeAdsServiceImpl implements MarqueeAdsService {

    @Autowired
    private MarqueeAdsDao marqueeAdsDao;

    @Override
    public List<MarqueeAds> getMarqueeAdsList() {
        return marqueeAdsDao.getMarqueeAdsList();
    }   

    @Override
    public MarqueeAds getMarqueeAdsById(Integer marqueeAdsId) {
        return marqueeAdsDao.getMarqueeAdsById(marqueeAdsId);
    }

    @Override
    public Integer createMarqueeAds(MarqueeAdsRequest marqueeAdsRequest) {
        return marqueeAdsDao.createMarqueeAds(marqueeAdsRequest);
    }

    @Override
    public void updateMarqueeAds(Integer marqueeAdsId, MarqueeAdsRequest marqueeAdsRequest) {
        marqueeAdsDao.updateMarqueeAds(marqueeAdsId, marqueeAdsRequest);
    }

    @Override
    public void deleteMarqueeAds(Integer marqueeAdsId) {
        marqueeAdsDao.deleteMarqueeAds(marqueeAdsId);
    }


    
}
