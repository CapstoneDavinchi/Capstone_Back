package Capstone.Davinchi_Server.chat.repository;

import Capstone.Davinchi_Server.chat.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    @Query("select uc from UserChatRoom uc where uc.user.userId <> :userId and uc.userChatRoomId = :userChatRoomId")
    Optional<UserChatRoom> findByUserChatRoomIdAndOtherUserId(@Param("userId") Long userId, @Param("userChatRoomId") Long userChatRoomId);

    @Query("select uc from UserChatRoom uc where uc.user.userId = :userId")
    List<UserChatRoom> findUserListByUserId(@Param("userId") Long userId);



}
