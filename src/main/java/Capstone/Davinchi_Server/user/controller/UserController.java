package Capstone.Davinchi_Server.user.controller;

import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponse;
import Capstone.Davinchi_Server.user.dto.Token;
import Capstone.Davinchi_Server.user.dto.UserReq;
import Capstone.Davinchi_Server.user.dto.UserRes;
import Capstone.Davinchi_Server.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@Tag(name="User❤", description = "User 관련 Api")
@RequestMapping("/jwt")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
//    @Operation(summary = "자체 회원가입", description = "자체 회원가입")
    public ApiResponse<UserRes.SignUpRes> signup(@RequestBody UserReq.SignUpReq signUpReq) throws Exception {
        try{
            return new ApiResponse<>(userService.signup(signUpReq));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/login")
//    @Operation(summary = "자체 로그인", description = "자체 로그인")
    public ApiResponse<Token> login(@RequestBody UserReq.LoginReq loginReq) throws Exception {
        try{
            return new ApiResponse<>(userService.login(loginReq));
        } catch (ApiException exception){
            return new ApiResponse<>(exception.getStatus());
        }
    }

}