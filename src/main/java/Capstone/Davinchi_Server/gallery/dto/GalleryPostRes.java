package Capstone.Davinchi_Server.gallery.dto;

import Capstone.Davinchi_Server.gallery.entity.enums.GalleryCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class GalleryPostRes {

    @Getter
    @Builder
    public static class AddGalleryPostRes{
        private Long id;
        private LocalDateTime createdDate;
    }

    @Getter
    @Builder
    public static class UpdateGalleryPostRes{
        private Long id;
        private LocalDateTime updateDate;
    }
}
