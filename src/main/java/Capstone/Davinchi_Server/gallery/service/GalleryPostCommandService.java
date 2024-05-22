package Capstone.Davinchi_Server.gallery.service;

import Capstone.Davinchi_Server.gallery.converter.GalleryImgConverter;
import Capstone.Davinchi_Server.gallery.converter.GalleryPostConverter;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostReq;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostImg;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.repository.GalleryPostImgRepository;
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

    public GalleryPostRes.AddGalleryPostRes addGalleryPost(GalleryPostReq.AddGalleryPostReq addGalleryPostReq, List<MultipartFile> images, String email){

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        GalleryPost galleryPost = GalleryPostConverter.toGalleryPost(addGalleryPostReq, currentUser);

        if(images != null){
            List<GalleryPostImg> imgs = images.stream()
                    .map(image -> {
                        String imgUrl = storageService.upload(image);
                        return GalleryImgConverter.toGalleryImg(image.getOriginalFilename(), imgUrl, galleryPost);
                    })
                    .collect(Collectors.toList());

            galleryPostImgRepository.saveAll(imgs);
        }


        galleryPostRepository.save(galleryPost);
        return GalleryPostConverter.toAddGalleryPostRes(galleryPost);
    }

    public GalleryPostRes.UpdateGalleryPostRes updateGalleryPost(GalleryPostReq updateGalleryPostReq, List<MultipartFile> images, String email){


    }
}
