package Capstone.Davinchi_Server.user.dto;

import lombok.*;

public class UserReq {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SignUpReq {
        private String email;
        private String password;
        private String nickname;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LoginReq {
        private String email;
        private String password;
    }
}
