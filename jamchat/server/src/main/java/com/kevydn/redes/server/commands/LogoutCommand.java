package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

public class LogoutCommand implements Command {

    @Override
    public String name() { return "/logout"; }

    @Override
    public String description() { return "Faz logout: /logout"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        Server.removeClient(ctx.getUsername());
        ClientHandler clientHandler = (ClientHandler) ctx;
        clientHandler.setUsername(null);
        ctx.send("/logoutok");
    }

    @Override
    public int expectedArgs() { return 0; }
}
