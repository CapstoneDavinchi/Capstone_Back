package Capstone.Davinchi_Server.gallery.dto;


import Capstone.Davinchi_Server.gallery.entity.enums.GalleryCategory;
import lombok.Getter;

public class GalleryPostReq {
    @Getter
    public static class AddGalleryPostReq{
        private String title;
        private String content;
        private GalleryCategory galleryCategory;
    }

    @Getter
    public static class UpdateGalleryPostReq{
        private Long id;
        private String title;
        private String content;
        private GalleryCategory galleryCategory;
    }

}
