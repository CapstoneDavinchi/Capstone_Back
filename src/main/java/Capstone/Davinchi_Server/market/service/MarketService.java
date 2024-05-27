package Capstone.Davinchi_Server.market.service;

import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import Capstone.Davinchi_Server.image.StorageService;
import Capstone.Davinchi_Server.market.dto.MarketReq;
import Capstone.Davinchi_Server.market.entity.MarketPost;
import Capstone.Davinchi_Server.market.repository.MarketRepository;
import Capstone.Davinchi_Server.user.entity.User;
import Capstone.Davinchi_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketService {

    private final MarketRepository marketRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
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
                    .build();
            marketRepository.save(marketPost);

            //TODO: 구글 클라우드에 사진 저장
            if (multipartFiles != null) {
               // String imgurl = storageService.upload(multipartFiles);
            }

            return "marketId: " + marketPost.getMarketId() + "인 게시글을 생성했습니다.";
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

                //TODO: 사진 업데이트, 지우고 다시 저장
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
            //TODO: 업로드한 게시글 사진들 삭제
            marketRepository.delete(market);
            return "요청하신 마켓 게시글의 삭제가 완료되었습니다.";
        } else {
            throw new ApiException(ApiResponseStatus.NOT_FOUND);
        }
    }





}
