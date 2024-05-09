package Capstone.Davinchi_Server.chat.controller;

import Capstone.Davinchi_Server.chat.dto.chatroom.ChatRoomRes;
import Capstone.Davinchi_Server.chat.service.ChatRoomService;
import Capstone.Davinchi_Server.chat.entity.ChatRoom;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/room")
    public ApiResponse<String> CreateChatRoom(@PathVariable Long AuthorId, Principal principal) {
        try {
            String chatRoomId = chatRoomService.createChatRoom(AuthorId, principal.getName());
            String result = chatRoomId + "번 채팅방을 생성하였습니다.";
            return new ApiResponse<>(result);
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    // 내가 속한 채팅방 리스트(+상대 프로필) 조회
    @GetMapping("/room")
    public ApiResponse<List<ChatRoomRes.GetChatRoomRes>> getChatRoomList(Principal principal) {
        try {
            return new ApiResponse<>(chatRoomService.getChatRoomListById(principal.getName()));

        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    // 원래 있던 채팅방 재입장시 화면에 나와야 할 정보를 반환
    @GetMapping("/room/enter/{roomId}")
    public ApiResponse<List<ChatRoomRes.GetChatRoomDetailRes>> getChatRoomDetails(@PathVariable String roomId) {
        try {
            return new ApiResponse<>(chatRoomService.getChatRoomDetails(roomId));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

}

