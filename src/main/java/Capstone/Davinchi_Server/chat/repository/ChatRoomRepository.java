package Capstone.Davinchi_Server.chat.repository;

import Capstone.Davinchi_Server.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Query("select c from ChatRoom c where c.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findChatRoomById(@Param("chatRoomId") String chatRoomId);


}
