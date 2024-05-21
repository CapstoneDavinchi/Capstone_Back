package Capstone.Davinchi_Server.gallery.repository;

import Capstone.Davinchi_Server.chat.entity.ChatRoom;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryPostRepository extends JpaRepository<GalleryPost, String> {
}
