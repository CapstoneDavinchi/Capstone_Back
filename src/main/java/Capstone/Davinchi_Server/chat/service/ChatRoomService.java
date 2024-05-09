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

    public List<ChatRoomRes.GetChatRoomRes> getChatRoomListById(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        List<UserChatRoom> userChatRooms = userChatRoomRepository.findUserListByUserId(user.getUserId());

        List<ChatRoomRes.GetChatRoomRes> getChatRoomRes = userChatRooms.stream()
                .map(userChatRoom -> {
                    // 나를 제외한 이 채팅룸에 있는 상대의 정보, 즉 채팅하는 대상 정보
                    UserChatRoom chatRoom = userChatRoomRepository.findByUserChatRoomIdAndOtherUserId(user.getUserId(), userChatRoom.getUserChatRoomId()).orElseThrow(() -> {
                        throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
                    });
                    String receiver = chatRoom.getUser().getNickname();
                    String profileImage = chatRoom.getUser().getProfileImage();
                    // 채팅방의 마지막 채팅메시지 정보
                    Optional<TextMessage> lastTextMessageOptional = textMessageRepository.findLatestMessageByRoomId(userChatRoom.getChatRoom().getChatRoomId());
                    TextMessage lastTextMessage = lastTextMessageOptional.orElse(null);
                    String latestMessage="";
                    String latestDate="";
                    String latestTime="";
                    if (lastTextMessage != null) {
                        latestMessage = lastTextMessage.getMessage();
                        latestDate = lastTextMessage.getSendDate();
                        latestTime = lastTextMessage.getSendTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime localTime = LocalTime.parse(latestTime, formatter);
                        latestTime = formatTime(localTime);
                    } else {
                        throw new ApiException(ApiResponseStatus.BAD_REQUEST);
                    }
                    int unreadCount = 0;

                    return new ChatRoomRes.GetChatRoomRes(userChatRoom.getChatRoom().getChatRoomId(),
                            latestMessage, latestDate, latestTime, unreadCount, receiver, profileImage);
                })
                .collect(Collectors.toList());
        return getChatRoomRes;
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