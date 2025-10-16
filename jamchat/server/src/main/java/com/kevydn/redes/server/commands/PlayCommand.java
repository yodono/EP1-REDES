package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.CommandContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.audio.AudioStreamer;

import java.io.IOException;

public class PlayCommand implements Command {

    @Override
    public String name() { return "/play"; }

    @Override
    public String description() { return "Inicia música: /play <nome_da_musica>"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String songName = cmd.arg(0);
        if (songName == null || songName.isBlank()) {
            ctx.send("/msg Uso: /play <nome_da_musica>");
            return;
        }

        try {
            new Thread(new AudioStreamer("/audio/songs/" + songName)).start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            ctx.send("/msg Erro ao tentar reproduzir " + songName + ". Tente novamente mais tarde.");
        }
    }

    @Override
    public int expectedArgs() { return 1; }
}
