package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.Server;

import java.io.File;

public class ListJamsCommand implements Command {

    @Override
    public String name() { return "/list_jams"; }

    @Override
    public String description() { return "Lista jams em curso e disponíveis: /list_jams"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        StringBuilder sb = new StringBuilder(">>> Jams:\\n");

        try {
            File songsDir = new File("resources/audio/songs");
            File[] songs = songsDir.listFiles((dir, name) -> name.endsWith(".wav"));

            if (songs == null || songs.length == 0) {
                sb.append("Nenhuma jam encontrada.\\n");
            } else {
                for (File song : songs) {
                    String songName = song.getName();
                    boolean isPlaying = Server.hasJam(songName);
                    int connectedCount = Server.getJamClients(songName).size();

                    sb.append(" - ")
                            .append(songName)
                            .append(" - ")
                            .append(isPlaying ? "(tocando) - " : "(não tocando)")
                            .append(isPlaying ? connectedCount + " escutando" : "")
                            .append("\\n");
                }
            }

            ctx.send("/msg " + sb);
        } catch (Exception e) {
            ctx.send("/msg Erro ao listar jams: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int expectedArgs() { return 0; }
}
