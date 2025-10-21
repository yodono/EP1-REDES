package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.audio.AudioStreamPlayer;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;

import java.util.Objects;

public class JoinJamCommand implements Command {

    @Override
    public String name() { return "/join_jam"; }

    @Override
    public String description() { return "Conecta a uma porta para receber pacotes de áudio via UDP"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        int port = Integer.parseInt(Objects.requireNonNull(cmd.arg(0)));

        new Thread(new AudioStreamPlayer(port)).start();
    }

    @Override
    public int expectedArgs() { return 1; }
}
