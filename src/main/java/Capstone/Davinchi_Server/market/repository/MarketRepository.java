package Capstone.Davinchi_Server.market.repository;

import Capstone.Davinchi_Server.market.entity.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<MarketPost, Long> {
    List<MarketPost> findAllByOrderByMarketIdDesc();

    @Query("SELECT mp FROM MarketPost mp WHERE mp.title LIKE %:keyword% OR mp.content LIKE %:keyword% ORDER BY mp.marketId DESC")
    List<MarketPost> findAllByTitleOrContentContaining(@Param("keyword") String keyword);

    @Modifying
    @Query(value = "UPDATE market_post SET like_cnt = like_cnt + 1 WHERE market_id = :marketId", nativeQuery = true)
    void incrementLikeCount(@Param("marketId") Long marketId);

    @Modifying
    @Query(value = "UPDATE market_post SET like_cnt = like_cnt - 1 WHERE market_id = :marketId", nativeQuery = true)
    void decrementLikeCount(@Param("marketId") Long marketId);


}
