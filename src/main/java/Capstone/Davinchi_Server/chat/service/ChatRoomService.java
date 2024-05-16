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
@Transactional
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
        String result = chatRoom.getChatRoomId() + "번 채팅방을 생성하였습니다.";
        return result;
    }


    public List<ChatRoomRes.GetChatRoomRes> getChatRoomListById(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        List<UserChatRoom> userChatRooms = userChatRoomRepository.findUserListByUserId(user.getUserId());

        List<ChatRoomRes.GetChatRoomRes> getChatRoomRes = userChatRooms.stream()
                .map(userChatRoom -> {
                    // 나를 제외한 이 채팅룸에 있는 상대의 정보, 즉 채팅하는 대상 정보
                    UserChatRoom chatRoom = userChatRoomRepository.findByUserChatRoomIdAndOtherUserId(user.getUserId(), userChatRoom.getChatRoom().getChatRoomId()).orElseThrow(() -> {
                        throw new ApiException(ApiResponseStatus.NONE_EXIST_CHATROOM);
                    });
                    String receiver = chatRoom.getUser().getNickname();
                    String profileImage = chatRoom.getUser().getProfileImage();
                    // 채팅방의 마지막 채팅메시지 정보
                    TextMessage lastTextMessage = textMessageRepository.findLatestMessageByRoomId(chatRoom.getChatRoom().getChatRoomId()).orElse(null);
                    String latestMessage="No messages";
                    String latestDate="";
                    String latestTime="";
                    if (lastTextMessage != null) {
                        latestMessage = lastTextMessage.getMessage();
                        latestDate = lastTextMessage.getSendDate();
                        latestTime = lastTextMessage.getSendTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime localTime = LocalTime.parse(latestTime, formatter);
                        latestTime = formatTime(localTime);
                    }
                    int unreadCount = 0;

                    return new ChatRoomRes.GetChatRoomRes(userChatRoom.getChatRoom().getChatRoomId(),
                            latestMessage, latestDate, latestTime, unreadCount, receiver, profileImage);
                })
                .collect(Collectors.toList());
        return getChatRoomRes;
    }


    public List<ChatRoomRes.GetChatRoomDetailRes> getChatRoomDetails(String roomId) {
        List<ChatRoomRes.GetChatRoomDetailRes> chatRoomDetailResList = new ArrayList<>(); // 유저에게 보여질 채팅방 정보
        List<TextMessage> textMessages = textMessageRepository.findMessagesByRoomId(roomId); // 채팅방에 있는 메시지 리스트 저장

        List<String> messageList = new ArrayList<>(); // 같은 유저의 연속된 메시지를 저장하는 리스트
        for (int i = 0; i < textMessages.size(); i++) { // 채팅 기록을 역순(최근 순)으로 조회
            TextMessage msg = textMessages.get(i);
            msg.setRead(true);
            User sender = msg.getSender();
            messageList.add(msg.getMessage()); // 리스트에 메시지를 add
            if (i < textMessages.size() - 1) { // ArrayIndexOutOfBoundsException에 대한 Handling
                TextMessage prevMsg = textMessages.get(i + 1); // 바로 이전 메시지
                User prevSender = prevMsg.getSender(); // 바로 이전 메시지의 발신자
                // 이전에 메시지를 보낸 유저와 이번 메시지를 보낸 유저가 같으면
                if (sender.getUserId().equals(prevSender.getUserId())) {
                    continue;
                }
            }
            String profileUrl=sender.getProfileImage();

            ChatRoomRes.GetChatRoomDetailRes chatRoomDetailRes = new ChatRoomRes.GetChatRoomDetailRes(
                    sender.getUserId(), sender.getNickname(), profileUrl,
                    new ArrayList<>(messageList), msg.getSendDate(), msg.getSendTime());

            chatRoomDetailResList.add(chatRoomDetailRes);
            messageList.clear();
        }
        return chatRoomDetailResList;
    }


    // 채팅방 나가기
    public String exitChatRoom(String roomId, String email) {
        ChatRoom chatroom = chatRoomRepository.findChatRoomById(roomId).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_CHATROOM);
        });
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.NONE_EXIST_USER);
        });

        userChatRoomRepository.deleteUserChatRoomByUserIdWithRoomId(user.getUserId(), roomId);
        userChatRoomRepository.deleteUserChatRoomsByRoomId(roomId);
        textMessageRepository.deleteMessageByRoomId(roomId);
        chatRoomRepository.deleteChatRoomById(roomId);

        String result = user.getNickname() + "님이 " + roomId + "번 채팅방을 나갔습니다.";
        return result;
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