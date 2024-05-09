package Capstone.Davinchi_Server.chat.repository;

import Capstone.Davinchi_Server.chat.entity.TextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TextMessageRepository extends JpaRepository<TextMessage, Long> {
    @Query("select t from TextMessage t where t.chatRoom.chatRoomId = :roomId")
    List<TextMessage> findMessagesByRoomId(@Param("roomId") String roomId);

    @Query(value = "SELECT * FROM text_message WHERE chat_room_id = :roomId ORDER BY send_date DESC, send_time DESC LIMIT 1", nativeQuery = true)
    Optional<TextMessage> findLatestMessageByRoomId(@Param("roomId") String roomId);

    @Modifying
    @Query("delete from TextMessage t where t.chatRoom.chatRoomId = :roomId")
    void deleteMessageByRoomId(@Param("roomId") String roomId);


}
