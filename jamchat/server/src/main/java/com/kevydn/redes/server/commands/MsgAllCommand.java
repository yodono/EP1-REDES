package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

public class MsgAllCommand extends MsgCommand {

    @Override
    public String name() { return "/all"; }

    @Override
    public String description() { return "Manda uma mensagem para todos: /all <mensagem>"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        String msg = validateMessage(cmd, ctx);
        if (msg == null) return;

        ClientHandler sender = (ClientHandler) ctx;
        String formatted = formatMessage(sender, msg, "all");
        sender.send(formatted);
        Server.broadcastMessage(formatted, sender);
    }

    @Override
    public int expectedArgs() {
        return 1;
    }
}
