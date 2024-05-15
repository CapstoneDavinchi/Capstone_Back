package Capstone.Davinchi_Server.chat.repository;

import Capstone.Davinchi_Server.chat.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    @Query("select uc from UserChatRoom uc where uc.user.userId <> :userId and uc.userChatRoomId = :userChatRoomId")
    Optional<UserChatRoom> findByUserChatRoomIdAndOtherUserId(@Param("userId") Long userId, @Param("userChatRoomId") Long userChatRoomId);

    @Query("select uc from UserChatRoom uc where uc.user.userId = :userId")
    List<UserChatRoom> findUserListByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from UserChatRoom uc where uc.chatRoom.chatRoomId = :chatRoomId")
    void deleteUserChatRoomsByRoomId(@Param("chatRoomId") String chatRoomId);

    @Modifying
    @Query("delete from UserChatRoom uc where uc.user.userId = :userId and uc.chatRoom.chatRoomId = :chatRoomId")
    void deleteUserChatRoomByUserIdWithRoomId(@Param("userId") Long userId, @Param("chatRoomId") String chatRoomId);



}
