package Capstone.Davinchi_Server.gallery.service;

import Capstone.Davinchi_Server.gallery.converter.GalleryImgConverter;
import Capstone.Davinchi_Server.gallery.converter.GalleryPostConverter;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostReq;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostImg;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostLike;
import Capstone.Davinchi_Server.gallery.repository.GalleryPostImgRepository;
import Capstone.Davinchi_Server.gallery.repository.GalleryPostLikeRepository;
import Capstone.Davinchi_Server.gallery.repository.GalleryPostRepository;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import Capstone.Davinchi_Server.image.StorageService;
import Capstone.Davinchi_Server.user.entity.User;
import Capstone.Davinchi_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryPostCommandService {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final GalleryPostRepository galleryPostRepository;
    private final GalleryPostImgRepository galleryPostImgRepository;
    private final GalleryPostQueryService galleryPostQueryService;
    private final GalleryPostLikeRepository galleryPostLikeRepository;

    public GalleryPostRes.AddGalleryPostRes addGalleryPost(GalleryPostReq.AddGalleryPostReq addGalleryPostReq, List<MultipartFile> images, String email) {

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        GalleryPost galleryPost = GalleryPostConverter.toGalleryPost(addGalleryPostReq, currentUser);

        //updateGalleryPostImgs(images, galleryPost);

        galleryPostRepository.save(galleryPost);
        return GalleryPostConverter.toAddGalleryPostRes(galleryPost);
    }

    public GalleryPostRes.UpdateGalleryPostRes updateGalleryPost(GalleryPostReq.UpdateGalleryPostReq updateGalleryPostReq) {

        GalleryPost galleryPost = galleryPostQueryService.findGalleryPost(updateGalleryPostReq.getId());
        galleryPost.update(updateGalleryPostReq.getTitle(), updateGalleryPostReq.getContent(), updateGalleryPostReq.getCategory());

        return GalleryPostConverter.toUpdateGalleryPostRes(galleryPost);
    }

    public GalleryPostRes.DeleteGalleryPostRes deleteGalleryPost(Long id){
        try{
            galleryPostRepository.deleteById(id);
            return GalleryPostConverter.toDeleteGalleryPostRes(id);
        }catch (Exception e){
            throw new ApiException(ApiResponseStatus.DELETE_GALLERY_POST_FAIL);
        }
    }

    public GalleryPostRes.AddGalleryPostLikeRes addGalleryPostLike(String email, Long id){
        User currentUser = userRepository.findByEmail(email).orElseThrow(
                () -> new ApiException(ApiResponseStatus.NONE_EXIST_USER));


        GalleryPostLike galleryPostLike = galleryPostLikeRepository.save(GalleryPostConverter.toGalleryPostLike(currentUser, galleryPostQueryService.findGalleryPost(id)));
        return GalleryPostConverter.toGalleryPostLikeRes(galleryPostLike);
    }

/* 이미지 관련 코드
    private void updateGalleryPostImgs(List<MultipartFile> images, GalleryPost galleryPost) {
        if (images != null) {
            List<GalleryPostImg> imgs = images.stream()
                    .map(image -> {
                        String imgUrl = storageService.upload(image);
                        return GalleryImgConverter.toGalleryImg(image.getOriginalFilename(), imgUrl, galleryPost);
                    })
                    .collect(Collectors.toList());

            galleryPostImgRepository.saveAll(imgs);
        }
    }

    private void deleteGalleryPostImgs(List<Long> deleteImgsId){
        List<GalleryPostImg> deleteImgs = galleryPostImgRepository.findAllById(deleteImgsId);
    }
 */
}
