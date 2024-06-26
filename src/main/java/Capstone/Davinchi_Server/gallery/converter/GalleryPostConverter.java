package Capstone.Davinchi_Server.gallery.converter;

import Capstone.Davinchi_Server.gallery.dto.GalleryPostReq;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostLike;
import Capstone.Davinchi_Server.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


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
                .galleryPostId(galleryPost.getId())
                .updateDate(galleryPost.getCreatedDate())
                .build();
    }

    public static GalleryPostRes.DeleteGalleryPostRes toDeleteGalleryPostRes(Long id){
        return GalleryPostRes.DeleteGalleryPostRes.builder()
                .galleryPostId(id)
                .deletedDate(LocalDateTime.now())
                .build();
    }

    public static GalleryPostRes.GalleryPostListRes toGalleryPostListResList(GalleryPost galleryPost){
        return GalleryPostRes.GalleryPostListRes.builder()
                .galleryPostId(galleryPost.getId())
                .nickname(galleryPost.getUser().getNickname())
                .title(galleryPost.getTitle())
                .content(galleryPost.getContent())
                .profileImg(galleryPost.getUser().getProfileImage())
                .build();
    }

    public static List<GalleryPostRes.GalleryPostListRes> toGalleryPostListRes(List<GalleryPost> galleryPosts){
        return galleryPosts.stream().map(GalleryPostConverter::toGalleryPostListResList).toList();
    }

    public static GalleryPostLike toGalleryPostLike(User user, GalleryPost galleryPost){
        return GalleryPostLike.builder()
                .user(user)
                .galleryPost(galleryPost)
                .build();
    }

    public static GalleryPostRes.AddGalleryPostLikeRes toGalleryPostLikeRes(GalleryPostLike galleryPostLike){
        return GalleryPostRes.AddGalleryPostLikeRes.builder()
                .galleryPostLikeId(galleryPostLike.getId())
                .createdDate(galleryPostLike.getCreatedDate())
                .build();
    }

    public static GalleryPostRes.DeleteGalleryPostLikeRes toDeleteGalleryPostLikeRes(Long id){
        return GalleryPostRes.DeleteGalleryPostLikeRes.builder()
                .galleryPostLikeId(id)
                .deletedDate(LocalDateTime.now())
                .build();
    }

}
