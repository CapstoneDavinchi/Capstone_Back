package Capstone.Davinchi_Server.market.repository;

import Capstone.Davinchi_Server.market.entity.MarketImg;
import Capstone.Davinchi_Server.market.entity.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPhotoRepository extends JpaRepository<MarketImg, Long> {
    void deleteAllByMarketPost(MarketPost marketPost);


}
