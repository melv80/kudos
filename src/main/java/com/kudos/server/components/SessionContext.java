package com.kudos.server.components;

import com.kudos.server.model.jpa.PictureChannel;
import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionContext {

    private PictureChannel channel;

    @Autowired
    private KudosCardService kudosCardService;

    @Autowired
    private UserRepository userRepository;

    public void setChannel(PictureChannel channel) {
        this.channel = channel;
    }

    public synchronized PictureChannel getChannel() {
        if (channel == null) {
            channel = kudosCardService.getPictureChannel();
        }
        return channel;
    }

    public User getCurrentUser() {
        String email = getAuthentication().getName();
        return userRepository.findUserByMail(email);
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
