package Capstone.Davinchi_Server.market.service;

import Capstone.Davinchi_Server.image.StorageService;
import Capstone.Davinchi_Server.market.dto.MarketRes;
import Capstone.Davinchi_Server.market.entity.MarketImg;
import Capstone.Davinchi_Server.market.entity.MarketPost;
import Capstone.Davinchi_Server.market.repository.MarketPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketPhotoService {
    private final MarketPhotoRepository marketPhotoRepository;
    private final StorageService storageService;

    public void saveMarketPhoto(List<MarketImg> marketImgList) {
        marketPhotoRepository.saveAll(marketImgList);
    }

    public void saveAllMarketPhotoByMarket(List<MarketRes.GetGDSRes> getGDSResList, MarketPost marketPost){
        List<MarketImg> marketImgs = new ArrayList<>();
        for (MarketRes.GetGDSRes getGDSRes : getGDSResList) {
            MarketImg newMarketImg = MarketImg.builder()
                    .imageUrl(getGDSRes.getImgUrl())
                    .originalFilename(getGDSRes.getFileName())
                    .build();
            marketImgs.add(newMarketImg);
            marketPost.addPhotoList(newMarketImg);
        }
        saveMarketPhoto(marketImgs);
    }

    public void deleteAllByMarket(MarketPost marketPost) {
        marketPhotoRepository.deleteAllByMarketPost(marketPost);
    }



}
