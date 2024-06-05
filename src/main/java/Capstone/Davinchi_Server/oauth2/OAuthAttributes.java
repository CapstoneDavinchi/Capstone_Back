package Capstone.Davinchi_Server.oauth2;

import Capstone.Davinchi_Server.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {
    //반환 유저 정보
    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String name;
    private String email;
    private String profileImageUrl;
    private String registrationId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributesKey,
                           String name, String email, String profileImageUrl, String registrationId) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.registrationId = registrationId;
    }

    //OAuthAttributes 객체 생성
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if ("google".equals(registrationId)) { //구글 로그인
            return ofGoogle(userNameAttributeName, attributes, registrationId);
        } else if ("apple".equals(registrationId)) { //애플 로그인
            return ofApple(userNameAttributeName, attributes, registrationId);
        }

        return null;
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String registrationId) {
        return OAuthAttributes.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profileImageUrl(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .registrationId(registrationId)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofApple(String userNameAttributeName, Map<String, Object> attributes, String registrationId) {
        return OAuthAttributes.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profileImageUrl(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .registrationId(registrationId)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .nickname(name)
                .email(email)
                .profileImage(profileImageUrl)
                .registrationId(registrationId)
                .role("ROLE_USER")
                .build();
    }
}
