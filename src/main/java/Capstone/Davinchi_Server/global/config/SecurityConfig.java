package Capstone.Davinchi_Server.global.config;
import Capstone.Davinchi_Server.global.exception.AuthenticationEntryPointHandler;
import Capstone.Davinchi_Server.global.jwt.JwtAuthenticationFilter;
import Capstone.Davinchi_Server.global.jwt.JwtTokenUtil;
import Capstone.Davinchi_Server.oauth2.CustomOAuth2UserService;
import Capstone.Davinchi_Server.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import Capstone.Davinchi_Server.oauth2.OAuth2AuthenticationFailureHandler;
import Capstone.Davinchi_Server.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/jwt/**", "/oauth2/authorization/**", "/oauth2/**", "/login/**").permitAll()
                                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntryPointHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Login ->
                                oauth2Login
                                        //
                                        .loginPage("/login")
                                        // url로 접속시 oauth2 로그인 요청
                                        .authorizationEndpoint(authorizationEndpoint ->
                                                        authorizationEndpoint
//                                                .baseUri("/oauth2/authorize/")
                                                                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
                                        )
                                        .redirectionEndpoint(redirectionEndpoint ->
                                                redirectionEndpoint.baseUri("/login/oauth2/code/**")
                                        )
                                        .userInfoEndpoint(userInfoEndpoint ->
                                                userInfoEndpoint.userService(customOAuth2UserService)
                                        )
                                        .successHandler(oAuth2AuthenticationSuccessHandler)
                                        .failureHandler(oAuth2AuthenticationFailureHandler)
                );
        return http.build();
    }
}