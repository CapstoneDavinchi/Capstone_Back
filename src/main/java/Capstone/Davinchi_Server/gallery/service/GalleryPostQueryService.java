package Capstone.Davinchi_Server.gallery.service;

import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.repository.GalleryPostRepository;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
