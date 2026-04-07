package net.prastars.messagesystem.repository;

import net.prastars.messagesystem.contant.UserConnectionStatus;
import net.prastars.messagesystem.dto.projection.InviterUserIdProjection;
import net.prastars.messagesystem.dto.projection.UserConnectionStatusProjection;
import net.prastars.messagesystem.entity.UserConnectionEntity;
import net.prastars.messagesystem.entity.UserConnectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, UserConnectionId> {

    Optional<UserConnectionStatusProjection> findByPartnerAUserIdAndPartnerBUserId(
            @NonNull Long partnerAUserId,
            @NonNull Long partnerBUserId);

    Optional<UserConnectionEntity> findByPartnerAUserIdAndPartnerBUserIdAndStatus(
            @NonNull Long partnerAUserId,
            @NonNull Long partnerBUserId,
            @NonNull UserConnectionStatus status);

    Optional<InviterUserIdProjection> findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
            @NonNull Long partnerAUserId,
            @NonNull Long partnerBUserId);

}
