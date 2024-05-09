package Capstone.Davinchi_Server.chat.service;

import Capstone.Davinchi_Server.chat.dto.chatroom.ChatRoomRes;
import Capstone.Davinchi_Server.chat.entity.ChatRoom;
import Capstone.Davinchi_Server.chat.entity.TextMessage;
import Capstone.Davinchi_Server.chat.repository.ChatRoomRepository;
import Capstone.Davinchi_Server.chat.entity.UserChatRoom;
import Capstone.Davinchi_Server.chat.repository.TextMessageRepository;
import Capstone.Davinchi_Server.chat.repository.UserChatRoomRepository;
import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import Capstone.Davinchi_Server.user.entity.User;
import Capstone.Davinchi_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;
    private final TextMessageRepository textMessageRepository;

    @Transactional
    public String createChatRoom(Long AuthorId, String email) {

        User Author = userRepository.findByUserId(AuthorId).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(UUID.randomUUID().toString())
                .textMessageList(new ArrayList<>())
                .build();

        UserChatRoom userChatRoom1 = new UserChatRoom();
        userChatRoom1.setChatRoom(chatRoom);
        userChatRoom1.setUser(Author);

        UserChatRoom userChatRoom2 = new UserChatRoom();
        userChatRoom2.setChatRoom(chatRoom);
        userChatRoom2.setUser(currentUser);

        chatRoomRepository.save(chatRoom);
        userChatRoomRepository.save(userChatRoom1);
        userChatRoomRepository.save(userChatRoom2);
        return chatRoom.getChatRoomId();
    }



    public static String formatTime(LocalTime time) {
        int hour = time.getHour();
        int min = time.getMinute();
        String meridiem = (hour >= 12) ? "오후" : "오전";
        if (hour >= 12) {
            hour -= 12;
        }
        return meridiem + " " + hour + ":" + String.format("%02d", min);
    }

}