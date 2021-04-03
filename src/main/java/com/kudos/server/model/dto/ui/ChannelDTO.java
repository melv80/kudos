package com.kudos.server.model.dto.ui;

import java.util.ArrayList;
import java.util.List;

public class ChannelDTO extends Identifiable {
    public List<String> memberNames = new ArrayList<>();


    public int cardCount;
}
