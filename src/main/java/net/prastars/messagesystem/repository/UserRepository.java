package net.prastars.messagesystem.repository;

import net.prastars.messagesystem.dto.projection.UsernameProjection;
import net.prastars.messagesystem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(@NonNull String username);

    Optional<UsernameProjection> findByUserId(@NonNull Long userId);

    Optional<UserEntity> findByConnectionInviteCode(@NonNull String connectionInviteCode);

}
