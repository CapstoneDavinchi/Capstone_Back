package Capstone.Davinchi_Server.gallery.controller;

import Capstone.Davinchi_Server.gallery.dto.GalleryPostReq;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.service.GalleryPostCommandService;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryPostController {
    private final GalleryPostCommandService galleryPostCommandService;
    @Operation(summary = "예술작품 등록 API")
    @PostMapping(value = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<GalleryPostRes.AddGalleryPostRes> addGalleryPost(@RequestPart GalleryPostReq.AddGalleryPostReq addGalleryPostReq,
                                                      @RequestPart(required = false) List<MultipartFile> images,
                                                      Principal principal){
        try {
            return new ApiResponse<>(galleryPostCommandService.addGalleryPost(addGalleryPostReq, images, principal.getName()));

        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "예술작품 게시글 수정 API")
    @PatchMapping(value = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<GalleryPostRes.UpdateGalleryPostRes> updateGalleryPost(@RequestPart GalleryPostReq.UpdateGalleryPostReq updateGalleryPostReq,
                                                                        @RequestPart(required = false) List<MultipartFile> images){
        try {
            return new ApiResponse<>(galleryPostCommandService.updateGalleryPost(updateGalleryPostReq));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

}
