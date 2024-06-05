package Capstone.Davinchi_Server.gallery.repository;

import Capstone.Davinchi_Server.gallery.dto.GalleryPostDetails;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GalleryPostRepository extends JpaRepository<GalleryPost, Long> {
    @Query("SELECT new Capstone.Davinchi_Server.gallery.dto.GalleryPostDetails(" +
            "u.nickname, p.title, p.content, p.category, " +
            "(SELECT COUNT(l) FROM GalleryPostLike l WHERE l.galleryPost.id = p.id), " +
            "(SELECT COUNT(f) FROM GalleryFeedback f WHERE f.galleryPost.id = p.id), " +
            "(SELECT COUNT(c) FROM GalleryComment c WHERE c.galleryPost.id = p.id), " +
            "p.createdDate) " +
            "FROM GalleryPost p " +
            "JOIN p.user u " +
            "WHERE p.id = :postId")
    GalleryPostDetails findPostDetailById(@Param("postId") Long postId);

}
