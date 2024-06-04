package Capstone.Davinchi_Server.gallery.controller;

import Capstone.Davinchi_Server.gallery.dto.GalleryPostDetails;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostReq;
import Capstone.Davinchi_Server.gallery.dto.GalleryPostRes;
import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.service.GalleryPostCommandService;
import Capstone.Davinchi_Server.gallery.service.GalleryPostQueryService;
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
    private final GalleryPostQueryService galleryPostQueryService;
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

    @Operation(summary = "예술작품 게시글 삭제 API")
    @DeleteMapping("/posts/{galleryPostId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<GalleryPostRes.DeleteGalleryPostRes> deleteGalleryPost(@PathVariable(name = "galleryPostId") Long id){
        try {
            return new ApiResponse<>(galleryPostCommandService.deleteGalleryPost(id));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "예술작품 게시글 상세 조회 API")
    @GetMapping("/posts/{galleryPostId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<GalleryPostDetails> getGalleryPost(@PathVariable(name = "galleryPostId") Long id){
        try {
            return new ApiResponse<>(galleryPostQueryService.getGalleryPostDetails(id));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "예술작품 게시글 목록 조회 API")
    @GetMapping("/posts")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<GalleryPostRes.GalleryPostListRes>> getGalleryPostList(){
        try {
            return new ApiResponse<>(galleryPostQueryService.getGalleryPostList());
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "예술작품 게시글 좋아요 API")
    @PostMapping("/posts/likes/{galleryPostId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<GalleryPostRes.AddGalleryPostLikeRes> addLike(@PathVariable(name = "galleryPostId") Long id, Principal principal){
        try {
            return new ApiResponse<>(galleryPostCommandService.addGalleryPostLike(principal.getName(), id));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }



}
