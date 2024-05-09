package Capstone.Davinchi_Server.chat.dto.chatroom;

import lombok.*;

import java.util.List;
import java.util.Objects;

public class ChatRoomRes {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GetChatRoomDetailRes {
        Long userId;
        String nickName;
        String profileImage;
        List<String> message;
        String sendDate;
        String sendTime;
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GetChatRoomRes {
        private String chatRoomId;
        private String latestMessage; // 가장 최근 메시지
        private String latestDate; // 가장 최근 메시지의 날짜
        private String latestTime; // 가장 최근 메시지의 시간
        private int unreadCount; // 읽지 않은 메시지 수
        private String receiver; // 채팅 상대 닉네임
        private String profileImage; // 채팅 상대 프로필 사진

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GetChatRoomRes that = (GetChatRoomRes) o;
            return Objects.equals(chatRoomId, that.chatRoomId) &&
                    Objects.equals(latestMessage, that.latestMessage) &&
                    Objects.equals(latestDate, that.latestDate) &&
                    Objects.equals(latestTime, that.latestTime) &&
                    Objects.equals(unreadCount, that.unreadCount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chatRoomId, latestMessage, latestDate, latestTime, unreadCount);
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GetUserRes {
        private Long userId;
        private String nickName;
        private String imgUrl;
        private String fileName;
    }

}
