package net.prastars.messagesystem.auth;

import net.prastars.messagesystem.entity.UserEntity;
import net.prastars.messagesystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageUserDetailService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MessageUserDetailService.class);
    private final UserRepository messageUserRepository;
    public MessageUserDetailService(UserRepository messageUserRepository) {
        this.messageUserRepository = messageUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity messageUserEntity = messageUserRepository.findByUsername(username).orElseThrow(() -> {
            log.info("User not found: {}", username);
            return new UsernameNotFoundException("");
        });

        return new MessageUserDetails(messageUserEntity.getUserId(), messageUserEntity.getUsername(), messageUserEntity.getPassword());
    }
}
