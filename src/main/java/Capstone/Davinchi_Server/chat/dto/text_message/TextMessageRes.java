package Capstone.Davinchi_Server.chat.dto.text_message;

import Capstone.Davinchi_Server.chat.entity.TextMessage;
import Capstone.Davinchi_Server.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

public class TextMessageRes {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ChatEnterRes {
        private TextMessage.TextMessageType type; // 메시지 타입
        private String message; // 메시지
        private String sendDate; // 채팅 발송 날짜
        private String sendTime; // 채팅 발송 시간
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ChatTalkRes {
        private TextMessage.TextMessageType type; // 메시지 타입
        private Long senderId; // 채팅을 보낸 사람
        private String senderNickName; // 보낸사람 닉네임
        private String message; // 메시지
        private String sendDate; // 채팅 발송 날짜
        private String sendTime; // 채팅 발송 시간
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ChatLeaveRes {
        private TextMessage.TextMessageType type; // 메시지 타입
        private Long senderId; // 채팅을 보낸 사람
        private String message; // 메시지
        private String sendDate; // 채팅 발송 날짜
        private String sendTime; // 채팅 발송 시간
    }


}
