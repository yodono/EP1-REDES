package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.Server;
import com.kevydn.redes.server.audio.AudioStreamer;

public class PlayCommand implements Command {

    @Override
    public String name() { return "/play"; }

    @Override
    public String description() { return "Inicia música: /play <nome_da_musica>"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        String songName = cmd.arg(0);
        if (songName == null || songName.isBlank()) {
            ctx.send("/msg Uso: /play <nome_da_musica>");
            return;
        }

        if (Server.hasJam(songName)) {
            ctx.send("/join_jam " + Server.getJamPort(songName));
            return;
        }

        Server.addJam(songName);
        int port = Server.getJamPort(songName);
        new Thread(new AudioStreamer("audio/songs/" + songName, port)).start();
        ctx.send("/join_jam " + port);
    }

    @Override
    public int expectedArgs() { return 1; }
}
