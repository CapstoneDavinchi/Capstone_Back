package Capstone.Davinchi_Server.market.dto;

import lombok.*;

import java.util.List;

public class MarketRes {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class MarketListRes {
        private String title; // 작품(상품)명
        private String writer; // 작성자
        private String writerImgUrl; // 작성자 프로필 사진
        private String price; // 작품(상품) 가격
        private GetGDSRes marketImg; // 작품, 상품 대표 사진
        private String content; // 작품 설명
        private String createdTime;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class MarketDetailRes {
        private String title; // 작품(상품)명
        private String writer; // 작성자
        private String category; // 카테고리
        private String writerImgUrl; // 작성자 프로필 사진
        private String price; // 작품(상품) 가격
        private List<GetGDSRes> marketImgs; // 작품, 상품 사진
        private String content; // 작품 설명
        private String like; // 작품 찜 수
        private String createdTime;
        private String createdDate;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GetGDSRes {
        private String imgUrl;
        private String fileName;
    }


}
