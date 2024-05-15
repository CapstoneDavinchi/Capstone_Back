package Capstone.Davinchi_Server.chat.service;

import Capstone.Davinchi_Server.chat.dto.text_message.TextMessageReq;
import Capstone.Davinchi_Server.chat.dto.text_message.TextMessageRes;
import Capstone.Davinchi_Server.chat.entity.ChatRoom;
import Capstone.Davinchi_Server.chat.entity.TextMessage;
import Capstone.Davinchi_Server.chat.repository.ChatRoomRepository;
import Capstone.Davinchi_Server.chat.repository.TextMessageRepository;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import Capstone.Davinchi_Server.user.entity.User;
import Capstone.Davinchi_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TextMessageService {
    private final TextMessageRepository textMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public TextMessageRes.ChatEnterRes enterUser(String roomId, TextMessageReq.ChatEnterReq chatEnterReq, SimpMessageHeaderAccessor headerAccessor) {
        User user = userRepository.findByUserId(chatEnterReq.getSenderId()).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });
        Long userId = chatEnterReq.getSenderId();
        // 반환 결과를 socket session 에 user ID 로 저장
        headerAccessor.getSessionAttributes().put("userId", userId); // getSessionAttributes()는 WebSocket 세션의 속성(attribute) 맵에 접근하는 메서드
        headerAccessor.getSessionAttributes().put("roomId", roomId);
        TextMessageRes.ChatEnterRes chatEnterRes= TextMessageRes.ChatEnterRes.builder()
                .type(TextMessage.TextMessageType.ENTER)
                .message((user.getNickname() + " 님이 입장하셨습니다."))
                .sendDate(chatEnterReq.getSendDate())
                .sendTime(chatEnterReq.getSendTime()).build();
        return chatEnterRes;
    }



    @Transactional
    public TextMessageRes.ChatTalkRes sendMessage(String roomId, TextMessageReq.ChatTalkReq chatTalkReq) {
        User sender = userRepository.findByUserId(chatTalkReq.getSenderId()).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_REVIEW);
        });

        TextMessage chatMessage = TextMessage.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .textMessageType(TextMessage.TextMessageType.TALK)
                .message(chatTalkReq.getMessage())
                .sendDate(chatTalkReq.getSendDate())
                .sendTime(chatTalkReq.getSendTime())
                .build();
        textMessageRepository.save(chatMessage);

        TextMessageRes.ChatTalkRes chatTalkRes= TextMessageRes.ChatTalkRes.builder()
                .senderId(sender.getUserId())
                .senderNickName(sender.getNickname())
                .type(TextMessage.TextMessageType.TALK)
                .message(chatTalkReq.getMessage())
                .sendDate(chatMessage.getSendDate())
                .sendTime(chatMessage.getSendTime()).build();
        return chatTalkRes;
    }
}