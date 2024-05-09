package Capstone.Davinchi_Server.chat.dto.text_message;

import Capstone.Davinchi_Server.chat.entity.TextMessage;
import lombok.*;

public class TextMessageReq {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostMessageReq {
        private TextMessage.TextMessageType textMessageType;
        private String roomId;
        private String message;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AddUserReq {
        private Long userId;
        private String roomId;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SendMessageReq {
        private String roomId;
        private String message;
    }

}
