package Capstone.Davinchi_Server.gallery.dto;

import Capstone.Davinchi_Server.gallery.entity.enums.GalleryCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GalleryPostRes {
    @Getter
    @AllArgsConstructor
    public static class GalleryPostDetailsRes{
        private String nickname;
        private String title;
        private String content;
        private GalleryCategory category;
        private Long LikeCount;
        private Long feedbackCount;
        private Long commentCount;
        private LocalDateTime createdDate;
    }

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

    @Getter
    @Builder
    public static class DeleteGalleryPostRes{
        private Long id;
        private LocalDateTime deletedDate;
    }
    @Getter
    @Builder
    public static class GalleryPostListRes{
        private Long id;
        private String nickname;
        private String title;
        private String content;
        private String profileImg;
    }

    @Getter
    @Builder
    public static class AddGalleryPostLikeRes{
        private Long id;
        LocalDateTime createdDate;
    }

    @Getter
    @Builder
    public static class DeleteGalleryPostLikeRes{
        private Long id;
        LocalDateTime deletedDate;
    }


}
