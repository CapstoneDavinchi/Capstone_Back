package Capstone.Davinchi_Server.chat.controller;

import Capstone.Davinchi_Server.chat.dto.text_message.TextMessageReq;
import Capstone.Davinchi_Server.chat.dto.text_message.TextMessageRes;
import Capstone.Davinchi_Server.chat.entity.TextMessage;
import Capstone.Davinchi_Server.chat.service.TextMessageService;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponse;
import Capstone.Davinchi_Server.user.entity.User;
import Capstone.Davinchi_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@RestController
public class TextMessageController {

    private final SimpMessageSendingOperations template;
    private final UserRepository userRepository;
    private final TextMessageService textMessageService;

    // localhost:8080/pub/enter/{roomId} URI로 WebSocket 접근시
    // 해당 요청이 서버에 도달 enterUser 메서드 실행, 이는 클라이언트가 채팅방에 입장할 때 trigger되는 메소드
    @MessageMapping("/enter/{roomId}")
    public ApiResponse<String> enterUser(@DestinationVariable String roomId, @Payload TextMessageReq.ChatEnterReq chatEnterReq, SimpMessageHeaderAccessor headerAccessor) {
        try {
            TextMessageRes.ChatEnterRes chatEnterRes = textMessageService.enterUser(roomId, chatEnterReq, headerAccessor);
            template.convertAndSend("/sub/chat/room/" + roomId, chatEnterRes);
            return new ApiResponse<>("채팅방 입장 처리를 완료하였습니다.");
        } catch (ApiException ignored) {
            return new ApiResponse<>(ignored.getStatus());
        }
    }

    // 메시지 보낼 때는 /sub/chat/room/{roomId}를 대상으로 메시지 전송
    // 클라이언트가 localhost:8080/pub/talk/{roomId} URI로 WebSocket 메시지 전송
    @MessageMapping("/talk/{roomId}")
    public ApiResponse<String> sendMessage(@DestinationVariable String roomId, @Payload TextMessageReq.ChatTalkReq chatTalkReq) {
        try{
            TextMessageRes.ChatTalkRes chatTalkRes = textMessageService.sendMessage(roomId, chatTalkReq);
            template.convertAndSend("/sub/chat/room/" + roomId, chatTalkRes);
            return new ApiResponse<>("채팅메시지 처리를 완료하였습니다.");
        } catch (ApiException ignored) {
            return new ApiResponse<>(ignored.getStatus());
        }
    }

    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    // WebSocket 세션이 끊어질 때(SessionDisconnectEvent) 트리거되는 메서드
    // URI에 직접 매핑되지 않고, WebSocket 세션이 끊어질 때 자동으로 호출된다.
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 user id와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");


        if (userId != null) {
            User user = userRepository.findByUserId(userId).orElse(null);
            TextMessageRes.ChatLeaveRes chatLeaveRes = TextMessageRes.ChatLeaveRes.builder()
                    .type(TextMessage.TextMessageType.LEAVE)
                    .senderId(userId)
                    .message(user.getNickname() + " 님의 연결이 끊어졌습니다.")
                    .sendTime(LocalDateTime.now().toString())
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chatLeaveRes);
        }
    }

}
