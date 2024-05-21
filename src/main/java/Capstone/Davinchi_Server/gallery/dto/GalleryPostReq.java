package Capstone.Davinchi_Server.gallery.dto;


import Capstone.Davinchi_Server.gallery.entity.enums.GalleryCategory;
import lombok.Getter;
@Getter
public class GalleryPostReq {
    private String title;
    private String content;
    private GalleryCategory galleryCategory;
}
