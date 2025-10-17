package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

public class LoginCommand implements Command {

    @Override
    public String name() { return "/login"; }

    @Override
    public String description() { return "Faz login: /login <nome_usuario>"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        String username = cmd.arg(0);
        if (username == null || username.isBlank()) {
            ctx.send("/msg Uso: /login <nome_usuario>");
            return;
        }

        ClientHandler client = (ClientHandler) ctx;

        if (client.getUsername() != null) {
            ctx.send("/msg Já está logado.");
            return;
        }

        client.setUsername(username);
        Server.addClient(username, client);
        ctx.send("/loginok " + username);
        ctx.send("/msg Login realizado com sucesso como " + username + "!");
    }

    @Override
    public int expectedArgs() { return 1; }
}
