package Capstone.Davinchi_Server.gallery.repository;

import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostLike;
import Capstone.Davinchi_Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryPostLikeRepository extends JpaRepository<GalleryPostLike,Long> {
    Long deleteByUserAndGalleryPost(User user, GalleryPost galleryPost);
}
