package Capstone.Davinchi_Server.market.repository;

import Capstone.Davinchi_Server.market.entity.MarketPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketLikeRepository extends JpaRepository<MarketPostLike, Long> {
    Optional<MarketPostLike> findByUserUserIdAndMarketPostMarketId(Long userId, Long marketId);
    List<MarketPostLike> findAllByUserUserIdOrderByMarketLikeIdDesc(Long userId);

}
