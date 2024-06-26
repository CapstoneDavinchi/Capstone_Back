package Capstone.Davinchi_Server.gallery.entity;

import Capstone.Davinchi_Server.gallery.entity.enums.GalleryCategory;
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
public class GalleryPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GalleryCategory category;

    @OneToMany(mappedBy = "galleryPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GalleryComment> GalleryComments;

    @OneToMany(mappedBy = "galleryPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GalleryPostLike> GalleryPostLikes;

    @OneToMany(mappedBy = "galleryPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GalleryFeedback> GalleryFeedbacks;

    @OneToMany(mappedBy = "galleryPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GalleryPostImg> galleryPostImgs;

    public void update(String title, String content, GalleryCategory category){
        this.title = title;
        this.content = content;
        this.category = category;
    }





}
