package net.prastars.messagesystem.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "message_user")
public class MessageUserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    public MessageUserEntity() {}

    public MessageUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        MessageUserEntity that = (MessageUserEntity) object;
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
