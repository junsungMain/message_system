package net.prastars.messagesystem.service;

import jakarta.transaction.Transactional;
import net.prastars.messagesystem.dto.domain.InviteCode;
import net.prastars.messagesystem.dto.domain.User;
import net.prastars.messagesystem.dto.domain.UserId;
import net.prastars.messagesystem.dto.projection.UsernameProjection;
import net.prastars.messagesystem.entity.UserEntity;
import net.prastars.messagesystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(SessionService sessionService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<String> getUsername(UserId userId){
        return userRepository.findByUserId(userId.id()).map(UsernameProjection::getUsername);
    }

    public Optional<User> getUser(InviteCode inviteCode){
        return userRepository.findByConnectionInviteCode(inviteCode.code()).
                map(entity -> new User(new UserId(entity.getUserId()), entity.getUsername()));
    }

    @Transactional
    public UserId addUser(String username, String password) {
        UserEntity messageUserEntity = userRepository.save(new UserEntity(username, passwordEncoder.encode(password)));
        log.info("User registered UserId: {}, Username: {}", messageUserEntity.getUserId(), messageUserEntity.getUsername());
        return new UserId(messageUserEntity.getUserId());
    }

    @Transactional
    public void removeUser() {
        String username = sessionService.getUsername();
        UserEntity messageUserEntity = userRepository.findByUsername(username).orElseThrow();
        userRepository.deleteById(messageUserEntity.getUserId());

        log.info("User unregistered. UserId: {}, Username: {}", messageUserEntity.getUserId(), messageUserEntity.getUsername());
    }
}
