package Capstone.Davinchi_Server.gallery.converter;

import Capstone.Davinchi_Server.gallery.dto.GalleryPostReq;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.image.StorageService;
import Capstone.Davinchi_Server.user.entity.User;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GalleryPostConverter {

    public static GalleryPost toGalleryPost(GalleryPostReq.AddGalleryPostReq addGalleryPostReq, User user){
        return GalleryPost.builder()
                .user(user)
                .title(addGalleryPostReq.getTitle())
                .content(addGalleryPostReq.getContent())
                .category(addGalleryPostReq.getGalleryCategory())
                .build();
    }

    public static GalleryPostRes.AddGalleryPostRes toAddGalleryPostRes(GalleryPost galleryPost){
        return GalleryPostRes.AddGalleryPostRes.builder()
                .id(galleryPost.getId())
                .createdDate(galleryPost.getCreatedDate())
                .build();
    }

    public static GalleryPostRes.UpdateGalleryPostRes toUpdateGalleryPostRes(GalleryPost galleryPost){
        return GalleryPostRes.UpdateGalleryPostRes.builder()
                .id(galleryPost.getId())
                .updateDate(galleryPost.getCreatedDate())
                .build();
    }

}
