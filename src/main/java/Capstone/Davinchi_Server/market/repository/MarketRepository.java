package Capstone.Davinchi_Server.market.repository;

import Capstone.Davinchi_Server.market.entity.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MarketRepository extends JpaRepository<MarketPost, Long> {
}
