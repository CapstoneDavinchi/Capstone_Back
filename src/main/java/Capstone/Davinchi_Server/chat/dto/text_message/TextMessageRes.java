package Capstone.Davinchi_Server.chat.dto.text_message;

import Capstone.Davinchi_Server.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

public class TextMessageRes {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostMessageRes {
        private User sender;
        private User receiver;
        private String message;
        private LocalDateTime sendTime;
    }


}
