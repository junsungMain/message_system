package net.prastars.messagesystem.auth;

import net.prastars.messagesystem.repository.MessageUserRepository;
import net.prastars.messagesystem.entity.MessageUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageUserDetailService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MessageUserDetailService.class);
    private final MessageUserRepository messageUserRepository;
    public MessageUserDetailService(MessageUserRepository messageUserRepository) {
        this.messageUserRepository = messageUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MessageUserEntity messageUserEntity = messageUserRepository.findByUsername(username).orElseThrow(() -> {
            log.info("User not found: {}", username);
            return new UsernameNotFoundException("");
        });

        return new MessageUserDetails(messageUserEntity.getUserId(), messageUserEntity.getUsername(), messageUserEntity.getPassword());
    }
}
