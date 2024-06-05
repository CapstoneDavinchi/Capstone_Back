package Capstone.Davinchi_Server.gallery.dto;

import Capstone.Davinchi_Server.gallery.entity.enums.GalleryCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class GalleryPostDetails {
    private String nickname;
    private String title;
    private String content;
    private GalleryCategory category;
    private Number LikeCount;
    private Number feedbackCount;
    private Number commentCount;
    private LocalDateTime createdDate;


}
