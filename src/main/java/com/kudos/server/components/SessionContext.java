package com.kudos.server.components;

import com.kudos.server.model.jpa.PictureChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionContext {

    private PictureChannel channel;

    @Autowired
    private KudosCardService kudosCardService;

    public void setChannel(PictureChannel channel) {
        this.channel = channel;
    }

    public synchronized PictureChannel getChannel() {
        if (channel == null) {
            channel = kudosCardService.getPictureChannel();
        }
        return channel;
    }
}
