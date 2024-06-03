package Capstone.Davinchi_Server.chat.dto.text_message;

import Capstone.Davinchi_Server.chat.entity.TextMessage;
import lombok.*;

public class TextMessageReq {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ChatEnterReq {
        private Long senderId; // 채팅을 보낸 유저id
        private String sendDate; // 채팅 발송 날짜
        private String sendTime; // 채팅 발송 시간
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ChatTalkReq {
        private Long senderId; // 채팅을 보낸 유저id
        private String message; // 메시지
        private String sendDate; // 채팅 발송 날짜
        private String sendTime; // 채팅 발송 시간
    }


}
