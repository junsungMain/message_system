package net.prastars.messagesystem.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "message_user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "connection_invite_code",nullable = false)
    private String connectionInviteCode;

    @Column(name = "connecteion_count",nullable = false)
    private int connectionCount;

    public UserEntity() {}

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.connectionInviteCode = UUID.randomUUID().toString().replace("-", "");
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConnectionInviteCode() {
        return connectionInviteCode;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        UserEntity that = (UserEntity) object;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", createAt=" + getCreateAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
