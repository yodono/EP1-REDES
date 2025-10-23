package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
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

        ClientHandler clientHandler = (ClientHandler) ctx;
        if (songName.equals(clientHandler.getJam())) {
            ctx.send("/msg Já está tocando - " + songName);
        }

        // se ja tiver tocando uma musica para esse cliente, desconectar e tocar a outra
        if (Server.getJamClients(clientHandler.getJam()).contains(clientHandler.getUsername())) {
            Server.removeClientFromJam(clientHandler.getJam(), ctx.getUsername());
        }

        Server.connectClientToJam(songName, clientHandler.getUsername());
        clientHandler.setJam(songName);

        if (Server.hasJam(songName)) {
            ctx.send("/join_jam " + Server.getJamPort(songName));
            return;
        }

        Server.addJam(songName);
        int port = Server.getJamPort(songName);
        new Thread(new AudioStreamer(songName, port)).start();
        ctx.send("/join_jam " + port);
    }

    @Override
    public int expectedArgs() { return 1; }
}
