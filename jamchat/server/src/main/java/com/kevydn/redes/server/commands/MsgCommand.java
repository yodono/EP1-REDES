package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

public class MsgCommand implements Command {

    @Override
    public String name() { return "/all"; }

    @Override
    public String description() { return "Manda uma mensagem para todos: /all <mensagem>"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        String msg = cmd.arg(0);
        if (msg == null || msg.isBlank()) {
            ctx.send("/msg Uso: /all <mensagem>");
            return;
        }

        ClientHandler client = (ClientHandler) ctx;
        String broadcastMessage = "/msg [" + client.getUsername() + "]: " + msg;
        Server.broadcastMessage(broadcastMessage, null); // TODO solucao paliativa, implementar isso no cliente (tentativa de envio de mensagem)
    }

    @Override
    public int expectedArgs() { return 1; }
}
