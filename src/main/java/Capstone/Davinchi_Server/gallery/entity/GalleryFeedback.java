package Capstone.Davinchi_Server.gallery.entity;

import Capstone.Davinchi_Server.global.entity.BaseTimeEntity;
import Capstone.Davinchi_Server.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GalleryFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallerPost_id")
    private GalleryPost galleryPost;

    @OneToMany(mappedBy = "galleryFeedback")
    private List<GalleryFeedbackImg> galleryFeedbackImgs;

    @Column(nullable = false)
    private String body;

}
