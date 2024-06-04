package Capstone.Davinchi_Server.gallery.service;

import Capstone.Davinchi_Server.gallery.converter.GalleryPostConverter;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostDetails;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.repository.GalleryPostRepository;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GalleryPostQueryService {

    private final GalleryPostRepository galleryPostRepository;

    public GalleryPost findGalleryPost(Long id){
        return galleryPostRepository.findById(id)
                .orElseThrow(() -> {
                    throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
                });
    }

    public GalleryPostDetails getGalleryPostDetails(Long id){
        return galleryPostRepository.findPostDetailById(id);
    }

    public List<GalleryPostRes.GalleryPostListRes> getGalleryPostList(){

        List<GalleryPost> galleryPosts = galleryPostRepository.findAll();
        if(galleryPosts.isEmpty()){
            throw new ApiException(ApiResponseStatus.GALLERY_POSTS_NOT_EXIST);
        }
        return GalleryPostConverter.toGalleryPostListRes(galleryPosts);
    }



}
