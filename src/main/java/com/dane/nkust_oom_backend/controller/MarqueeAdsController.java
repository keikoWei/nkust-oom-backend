package com.dane.nkust_oom_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import jakarta.validation.Valid;
import com.dane.nkust_oom_backend.model.MarqueeAds;
import com.dane.nkust_oom_backend.service.MarqueeAdsService;
import com.dane.nkust_oom_backend.dto.MarqueeAdsRequest;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@RequestMapping("/api")
public class MarqueeAdsController {

    @Autowired
    private MarqueeAdsService marqueeAdsService;

    // 取得所有跑馬燈廣告
    @GetMapping("/marquee-ads")
    public ResponseEntity<List<MarqueeAds>> getMarqueeAds() {
        
        List<MarqueeAds> marqueeAdsList = marqueeAdsService.getMarqueeAdsList();

        return ResponseEntity.status(HttpStatus.OK).body(marqueeAdsList);
    }

    // 取得單一跑馬燈廣告
    @GetMapping("/marquee-ads/{marqueeAdsId}")
    public ResponseEntity<MarqueeAds> getMarqueeAds(@PathVariable Integer marqueeAdsId) {

        MarqueeAds marqueeAds = marqueeAdsService.getMarqueeAdsById(marqueeAdsId);

        if (marqueeAds != null) {
            return ResponseEntity.status(HttpStatus.OK).body(marqueeAds);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 新增跑馬燈廣告
    @PostMapping("/marquee-ads")
    public ResponseEntity<MarqueeAds> createMarqueeAds(@RequestBody @Valid MarqueeAdsRequest marqueeAdsRequest) {

        Integer marqueeAdsId = marqueeAdsService.createMarqueeAds(marqueeAdsRequest);
        MarqueeAds marqueeAds = marqueeAdsService.getMarqueeAdsById(marqueeAdsId);
        return ResponseEntity.status(HttpStatus.CREATED).body(marqueeAds);
    }

    // 更新跑馬燈廣告
    @PutMapping("/marquee-ads/{marqueeAdsId}")
    public ResponseEntity<MarqueeAds> updateMarqueeAds(@PathVariable Integer marqueeAdsId, @RequestBody @Valid MarqueeAdsRequest marqueeAdsRequest) {

        // 檢查跑馬燈廣告是否存在
        MarqueeAds marqueeAds = marqueeAdsService.getMarqueeAdsById(marqueeAdsId);
        if (marqueeAds == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        marqueeAdsService.updateMarqueeAds(marqueeAdsId, marqueeAdsRequest);
        return ResponseEntity.status(HttpStatus.OK).body(marqueeAdsService.getMarqueeAdsById(marqueeAdsId));
    }       

    // 刪除跑馬燈廣告
    @DeleteMapping("/marquee-ads/{marqueeAdsId}")
    public ResponseEntity<Void> deleteMarqueeAds(@PathVariable Integer marqueeAdsId) {
        
        marqueeAdsService.deleteMarqueeAds(marqueeAdsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
