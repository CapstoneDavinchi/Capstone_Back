package Capstone.Davinchi_Server.market.dto;

import lombok.*;
public class MarketReq {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostMarketReq {
        private String category;
        private String title;
        private String content;
        private String price;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PatchMarketReq {
        private Long marketId;
        private String category;
        private String title;
        private String content;
        private String price;
    }


}
