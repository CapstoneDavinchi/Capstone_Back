package Capstone.Davinchi_Server.market.controller;

import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponse;
import Capstone.Davinchi_Server.market.dto.MarketReq;
import Capstone.Davinchi_Server.market.dto.MarketRes;
import Capstone.Davinchi_Server.market.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "마켓 게시글 API")
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;

    @Operation(summary = "마켓 게시글 등록 API")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<String> createMarketPost(Principal principal, @RequestPart(value="image", required = false) List<MultipartFile> multipartFiles,
                                                @RequestPart(value="postMarketReq") MarketReq.PostMarketReq postMarketReq) {
        try {
            return new ApiResponse<>(marketService.createMarket(principal.getName(), postMarketReq, multipartFiles));
        }
        catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "마켓 게시글 수정 API")
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<String> modifyMarketPost(Principal principal, @RequestPart(value = "image", required = false) List<MultipartFile> multipartFiles,
                                                @RequestPart(value = "patchMarketReq") MarketReq.PatchMarketReq patchMarketReq) {
        try {
            return new ApiResponse<>(marketService.modifyMarket(principal.getName(), patchMarketReq, multipartFiles));
        }
        catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }
    @Operation(summary = "마켓 게시글 삭제 API")
    @DeleteMapping("/{marketId}")
    public ApiResponse<String> deleteMarketPost(Principal principal, @PathVariable(name = "marketId") Long marketId){
        try{
            return new ApiResponse<>(marketService.deleteMarket(principal.getName(), marketId));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }
    @Operation(summary = "마켓 게시글 전체조회 API")
    @GetMapping
    public ApiResponse<List<MarketRes.MarketListRes>> getMarketPosts(Principal principal) {
        try{
            return new ApiResponse<>(marketService.getMarketPosts(principal.getName()));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }
    @Operation(summary = "마켓 게시글 상세조회 API")
    @GetMapping("/{marketId}")
    public ApiResponse<MarketRes.MarketDetailRes> getMarketDetailPost(Principal principal, @PathVariable(name = "marketId") Long marketId) {
        try{
            return new ApiResponse<>(marketService.getMarketDetailPost(principal.getName(), marketId));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "마켓 게시글 좋아요/좋아요 취소 API")
    @PostMapping("/like/{marketId}")
    public ApiResponse<String> likeMarket(Principal principal, @PathVariable(name="marketId") Long marketId){
        try{
            return new ApiResponse<>(marketService.likeMarket(principal.getName(), marketId));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }
    @Operation(summary = "마켓 게시글 키워드 검색 API")
    @GetMapping("/search")
    public ApiResponse<List<MarketRes.MarketListRes>> searchMarket(Principal principal, @RequestParam("keyword") String keyword){
        try{
            return new ApiResponse<>(marketService.searchMarket(principal.getName(), keyword));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @Operation(summary = "내가 찜한 마켓 게시글 전체조회 API")
    @GetMapping("/my")
    public ApiResponse<List<MarketRes.MarketListRes>> myMarket(Principal principal){
        try{
            return new ApiResponse<>(marketService.myMarket(principal.getName()));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }


}
