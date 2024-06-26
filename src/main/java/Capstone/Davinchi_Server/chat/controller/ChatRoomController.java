package Capstone.Davinchi_Server.chat.controller;

import Capstone.Davinchi_Server.chat.dto.chatroom.ChatRoomRes;
import Capstone.Davinchi_Server.chat.service.ChatRoomService;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅 API")
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @Operation(summary = "채팅방 생성 API")
    @PostMapping("/room/{authorId}")
    public ApiResponse<String> CreateChatRoom(@PathVariable Long authorId, Principal principal) {
        try {
            String result = chatRoomService.createChatRoom(authorId, principal.getName());
            return new ApiResponse<>(result);
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    // 내가 속한 채팅방 리스트(+상대 프로필) 조회
    @Operation(summary = "내가 속한 채팅방 리스트 조회 API")
    @GetMapping("/room")
    public ApiResponse<List<ChatRoomRes.GetChatRoomRes>> getChatRoomList(Principal principal) {
        try {
            return new ApiResponse<>(chatRoomService.getChatRoomListById(principal.getName()));

        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    // 채팅방 재입장시 화면에 나와야 할 정보(채팅정보)를 반환
    @Operation(summary = "채팅방 재입장 시 채팅정보 조회 API")
    @GetMapping("/room/enter/{roomId}")
    public ApiResponse<List<ChatRoomRes.GetChatRoomDetailRes>> getChatRoomDetails(@PathVariable String roomId) {
        try {
            return new ApiResponse<>(chatRoomService.getChatRoomDetails(roomId));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }


    // 채팅방 나가기/삭제, 이 api 호출시 db에서 해당 room 완전 삭제(거래 1:1진행 전제)
    @Operation(summary = "채팅방 삭제 API")
    @DeleteMapping("/room/{roomId}")
    public ApiResponse<String> exitChatRoom(@PathVariable String roomId, Principal principal){
        try {
            return new ApiResponse<>(chatRoomService.exitChatRoom(roomId, principal.getName()));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }




}

