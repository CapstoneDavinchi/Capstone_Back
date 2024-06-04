package Capstone.Davinchi_Server.gallery.repository;

import Capstone.Davinchi_Server.gallery.entity.GalleryPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryPostLikeRepository extends JpaRepository<GalleryPostLike,Long> {
}
