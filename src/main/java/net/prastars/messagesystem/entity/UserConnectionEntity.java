package net.prastars.messagesystem.entity;

import jakarta.persistence.*;
import net.prastars.messagesystem.contant.UserConnectionStatus;

import java.util.Objects;

@Entity
@Table(name = "user_connection")
@IdClass(UserConnectionId.class)
public class UserConnectionEntity extends BaseEntity{

    @Id
    @Column(name = "partner_a_user_id", nullable = false)
    private Long partnerAUserId;

    @Id
    @Column(name = "partner_b_user_id", nullable = false)
    private Long partnerBUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserConnectionStatus status;

    @Column(name = "inviter_user_id", nullable = false)
    private Long inviterUserId;

    public UserConnectionEntity(){}

    public UserConnectionEntity(Long partnerAUserId, Long partnerBUserId, UserConnectionStatus status, Long inviterUserId) {
        this.partnerAUserId = partnerAUserId;
        this.partnerBUserId = partnerBUserId;
        this.status = status;
        this.inviterUserId = inviterUserId;
    }

    public Long getPartnerAUserId() {
        return partnerAUserId;
    }

    public Long getPartnerBUserId() {
        return partnerBUserId;
    }

    public UserConnectionStatus getUserConnectionStatus() {
        return status;
    }

    public Long getInviterUserId() {
        return inviterUserId;
    }

    public void setUserConnectionStatus(UserConnectionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserConnectionEntity that = (UserConnectionEntity) o;
        return Objects.equals(partnerAUserId, that.partnerAUserId) && Objects.equals(partnerBUserId, that.partnerBUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partnerAUserId, partnerBUserId);
    }

    @Override
    public String toString() {
        return "UserConnectionEntity{" +
                "partnerAUserId=" + partnerAUserId +
                ", partnerBUserId=" + partnerBUserId +
                ", status=" + status +
                ", inviterUserId=" + inviterUserId +
                '}';
    }
}
