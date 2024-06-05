package Capstone.Davinchi_Server.market.service;

import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import Capstone.Davinchi_Server.image.StorageService;
import Capstone.Davinchi_Server.market.dto.MarketReq;
import Capstone.Davinchi_Server.market.dto.MarketRes;
import Capstone.Davinchi_Server.market.entity.MarketPost;
import Capstone.Davinchi_Server.market.repository.MarketRepository;
import Capstone.Davinchi_Server.user.entity.User;
import Capstone.Davinchi_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MarketService {

    private final MarketRepository marketRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final MarketPhotoService marketPhotoService;
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public String createMarket(String email, MarketReq.PostMarketReq postMarketReq, List<MultipartFile> multipartFiles) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
            });

            MarketPost marketPost = MarketPost.builder()
                    .title(postMarketReq.getTitle())
                    .category(postMarketReq.getCategory())
                    .content(postMarketReq.getContent())
                    .price(postMarketReq.getPrice())
                    .user(user)
                    .marketImgs(new ArrayList<>())
                    .build();
            marketRepository.save(marketPost);

            // 사진 저장
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                List<MarketRes.GetGDSRes> getGDSResList = new ArrayList<>();
                for (MultipartFile multipartFile : multipartFiles) {
                    String imgUrl = storageService.upload(multipartFile);
                    MarketRes.GetGDSRes getGDSRes = new MarketRes.GetGDSRes(imgUrl, multipartFile.getOriginalFilename());
                    getGDSResList.add(getGDSRes);
                }
            marketPhotoService.saveAllMarketPhotoByMarket(getGDSResList, marketPost);
            }
            String result="marketId: " + marketPost.getMarketId() + "인 게시글을 생성했습니다.";
            return result;
        } catch (ApiException exception) {
            throw new ApiException(exception.getStatus());
        }
    }


    public String modifyMarket(String email, MarketReq.PatchMarketReq patchMarketReq,
                              List<MultipartFile> multipartFiles) {
        try {
            MarketPost market = marketRepository.findById(patchMarketReq.getMarketId()).orElseThrow(() -> {
                throw new ApiException(ApiResponseStatus.NONE_EXIST_MARKET);
            });

            User writer = market.getUser();
            User visitor = userRepository.findByEmail(email).get();
            if (writer.getUserId() == visitor.getUserId()) {
                market.updateMarket(patchMarketReq.getTitle(), patchMarketReq.getContent(), patchMarketReq.getPrice(), patchMarketReq.getCategory());

                // 사진 업데이트: 기존 사진 삭제 후 새 사진 저장
                marketPhotoService.deleteAllByMarket(market);  // 이 메소드는 모든 사진을 삭제하는 역할을 합니다.

                if (multipartFiles != null && !multipartFiles.isEmpty()) {
                    List<MarketRes.GetGDSRes> getGDSResList = new ArrayList<>();
                    for (MultipartFile multipartFile : multipartFiles) {
                        String imgUrl = storageService.upload(multipartFile);
                        MarketRes.GetGDSRes getGDSRes = new MarketRes.GetGDSRes(imgUrl, multipartFile.getOriginalFilename());
                        getGDSResList.add(getGDSRes);
                    }
                    marketPhotoService.saveAllMarketPhotoByMarket(getGDSResList, market);
                }
                return "marketId " + market.getMarketId() + "의 게시글을 수정했습니다.";
            } else {
                throw new ApiException(ApiResponseStatus.NOT_FOUND);
            }
        } catch (ApiException exception) {
            throw new ApiException(exception.getStatus());
        }
    }


    public String deleteMarket(String email, Long marketId) {

        MarketPost market = marketRepository.findById(marketId).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_MARKET);
        });
        Long writerId = marketRepository.findById(marketId).get().getUser().getUserId();
        Long visitorId = userRepository.findByEmail(email).get().getUserId();

        if (writerId == visitorId) {
            // 업로드한 게시글 사진들 삭제
            marketPhotoService.deleteAllByMarket(market);

            marketRepository.delete(market);
            return "요청하신 마켓 게시글의 삭제가 완료되었습니다.";
        } else {
            throw new ApiException(ApiResponseStatus.NOT_FOUND);
        }
    }


    public List<MarketRes.MarketListRes> getMarketPosts(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
            });

            List<MarketPost> marketPosts = marketRepository.findAllByOrderByMarketIdDesc();
            List<MarketRes.MarketListRes> getMarketRes = marketPosts.stream()
                    .map(market -> {
                        // 대표 이미지를 설정 (첫 번째 이미지 사용)
                        MarketRes.GetGDSRes marketImg = market.getMarketImgs().isEmpty() ? null :
                                new MarketRes.GetGDSRes(
                                        market.getMarketImgs().get(0).getImageUrl(),
                                        market.getMarketImgs().get(0).getOriginalFilename()
                                );

                        return MarketRes.MarketListRes.builder()
                                .title(market.getTitle())
                                .writer(market.getUser().getNickname())
                                .writerImgUrl(market.getUser().getProfileImage())
                                .price(formatPrice(Integer.valueOf(market.getPrice())))
                                .marketImg(marketImg)
                                .content(market.getContent())
                                .createdTime(convertLocalDateTimeToTime(market.getCreatedDate()))
                                .build();
                    })
                    .collect(Collectors.toList());

            return getMarketRes;
        } catch (Exception exception) {
            throw new ApiException(ApiResponseStatus.NOT_FOUND);
        }
    }


    public MarketRes.MarketDetailRes getMarketDetailPost(String email, Long marketId) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
            });
            MarketPost market = marketRepository.findById(marketId).orElseThrow(() -> {
                throw new ApiException(ApiResponseStatus.NONE_EXIST_MARKET);
            });

            // MarketPost에 연결된 모든 이미지 URL을 수집
            List<MarketRes.GetGDSRes> marketImgs = market.getMarketImgs().stream()
                    .map(photo -> new MarketRes.GetGDSRes(photo.getImageUrl(), photo.getOriginalFilename()))
                    .collect(Collectors.toList());

            // MarketDetailRes 객체 생성
            MarketRes.MarketDetailRes marketDetailRes = MarketRes.MarketDetailRes.builder()
                    .title(market.getTitle())
                    .writer(market.getUser().getNickname())
                    .category(market.getCategory())
                    .writerImgUrl(market.getUser().getProfileImage())
                    .price(formatPrice(Integer.valueOf(market.getPrice())))
                    .marketImgs(marketImgs)
                    .content(market.getContent())
                    .like(market.getMarketPostLikes().toString())
                    .createdTime(convertLocalDateTimeToTime(market.getCreatedDate()))
                    .createdDate(convertLocalDateTimeToLocalDate(market.getCreatedDate()))
                    .build();

            return marketDetailRes;
        } catch (Exception exception) {
            throw new ApiException(ApiResponseStatus.NOT_FOUND);
        }
    }

    public static String convertLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public static String convertLocalDateTimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -

        if (diffTime < SEC){
            return diffTime + "초 전";
        }
        diffTime = diffTime / SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime = diffTime / MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime = diffTime / HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }
        diffTime = diffTime / DAY;
        if (diffTime < MONTH) {
            return diffTime + "개월 전";
        }
        diffTime = diffTime / MONTH;
        return diffTime + "년 전";
    }

    public static String formatPrice(Integer price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(price);
    }

}
