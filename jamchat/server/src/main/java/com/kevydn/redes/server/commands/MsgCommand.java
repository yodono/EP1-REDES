package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.CommandContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

public class MsgCommand implements Command {

    @Override
    public String name() { return "/all"; }

    @Override
    public String description() { return "Manda uma mensagem para todos: /all <mensagem>"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String msg = cmd.arg(0);
        if (msg == null || msg.isBlank()) {
            ctx.send("/msg Uso: /all <mensagem>");
            return;
        }

        ClientHandler client = (ClientHandler) ctx;
        String broadcastMessage = "/msg [" + client.getUsername() + "]: " + msg;
        Server.broadcastMessage(broadcastMessage, client);
    }

    @Override
    public int expectedArgs() { return 1; }
}
