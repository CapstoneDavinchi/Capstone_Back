package Capstone.Davinchi_Server.market.entity;

import Capstone.Davinchi_Server.gallery.entity.GalleryFeedback;
import Capstone.Davinchi_Server.gallery.entity.GalleryImg;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostLike;
import Capstone.Davinchi_Server.global.entity.BaseTimeEntity;
import Capstone.Davinchi_Server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MarketPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marketId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "marketPost")
    private List<MarketPostLike> marketPostLikes;

    @OneToMany(mappedBy = "marketPost")
    private List<MarketImg> marketImgs;
}

