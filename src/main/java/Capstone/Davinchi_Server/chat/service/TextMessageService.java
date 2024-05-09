package Capstone.Davinchi_Server.chat.service;

import Capstone.Davinchi_Server.chat.dto.text_message.TextMessageDto;
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
@Transactional
@RequiredArgsConstructor
public class TextMessageService {
    private final TextMessageRepository textMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;
    private final ChatRoomRepository chatRoomRepository;

    public void enterUser(TextMessageDto textMessageDto, SimpMessageHeaderAccessor headerAccessor) {
        User user = userRepository.findByUserId(textMessageDto.getSenderId()).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });
        Long userId = textMessageDto.getSenderId();
        // 반환 결과를 socket session 에 user ID 로 저장
        headerAccessor.getSessionAttributes().put("userId", userId); // getSessionAttributes()는 WebSocket 세션의 속성(attribute) 맵에 접근하는 메서드
        headerAccessor.getSessionAttributes().put("roomId", textMessageDto.getRoomId());
        textMessageDto.setMessage(user.getNickname() + " 님이 입장하셨습니다.");
        template.convertAndSend("/sub/messageDto/room/" + textMessageDto.getRoomId(), textMessageDto);
    }

    public void sendMessage(TextMessageDto textMessageDto) {
        User sender = userRepository.findByUserId(textMessageDto.getSenderId()).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(textMessageDto.getRoomId()).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_REVIEW);
        });

        TextMessage chatMessage = TextMessage.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .textMessageType(textMessageDto.getType())
                .message(textMessageDto.getMessage())
                .sendDate(textMessageDto.getSendDate())
                .sendTime(textMessageDto.getSendTime())
                .build();
        textMessageRepository.save(chatMessage);


    }
}