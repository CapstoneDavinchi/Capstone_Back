package Capstone.Davinchi_Server.oauth2;

import Capstone.Davinchi_Server.global.jwt.JwtTokenUtil;
import Capstone.Davinchi_Server.user.dto.Token;
import Capstone.Davinchi_Server.user.entity.RefreshToken;
import Capstone.Davinchi_Server.user.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static Capstone.Davinchi_Server.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        response.sendRedirect(targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Token token = jwtTokenUtil.createToken(email);

        // DB에 리프레시 토큰 저장 또는 업데이트
        refreshTokenRepository.findByKeyId(email).ifPresentOrElse(
                existingToken -> existingToken.update(token.getRefreshToken()),
                () -> refreshTokenRepository.save(new RefreshToken(email, token.getRefreshToken()))
        );

        // 쿠키에 토큰 저장
        addTokenToCookies(response, token.getAccessToken(), token.getRefreshToken());

        return redirectUri.orElse(getDefaultTargetUrl());
    }

    private void addTokenToCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) jwtTokenUtil.getAccessTokenExpiryDuration() / 1000);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) jwtTokenUtil.getRefreshTokenExpiryDuration() / 1000);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getDefaultTargetUrl() {
        return "https://davinchi-cr-ljqllr5pdq-an.a.run.app";
    }
}
