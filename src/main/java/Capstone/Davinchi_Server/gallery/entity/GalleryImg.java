package Capstone.Davinchi_Server.gallery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GalleryImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallerPost_id")
    private GalleryPost galleryPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galleryFeedback_id")
    private GalleryFeedback galleryFeedback;
}
