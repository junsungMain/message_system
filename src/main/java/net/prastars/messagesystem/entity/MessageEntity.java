package net.prastars.messagesystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_sequence", nullable = false)
    private Long messageSequence;

    @Column(name = "user_name", nullable = false)
    private String user_name;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    public MessageEntity() {
    }

    public MessageEntity(String user_name, String content) {
        this.user_name = user_name;
        this.content = content;
    }

    public Long getMessageSequence() {
        return messageSequence;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    public void PrePersist() {
        this.createAt = LocalDateTime.now();
        this.updatedAt = this.createAt;
    }

    @PreUpdate
    public void PreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        MessageEntity that = (MessageEntity) object;
        return Objects.equals(messageSequence, that.messageSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messageSequence);
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "messageSequence='" + messageSequence + '\'' +
                ", user_name='" + user_name + '\'' +
                ", content=" + content +
                ", createAt=" + createAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
