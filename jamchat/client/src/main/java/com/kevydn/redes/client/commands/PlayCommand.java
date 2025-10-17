package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.audio.AudioStreamPlayer;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;

public class PlayCommand implements Command {

    @Override
    public String name() { return "/play"; }

    @Override
    public String description() { return "Toca uma música"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        new Thread(new AudioStreamPlayer()).start();
    }

    @Override
    public int expectedArgs() { return 0; }
}
