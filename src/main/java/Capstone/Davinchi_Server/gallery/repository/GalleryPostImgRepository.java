package Capstone.Davinchi_Server.gallery.repository;

import Capstone.Davinchi_Server.gallery.entity.GalleryPostImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryPostImgRepository extends JpaRepository<GalleryPostImg,String> {
  //  List<GalleryPostImg> findAllById(List<Long> deleteImgsId);
}
