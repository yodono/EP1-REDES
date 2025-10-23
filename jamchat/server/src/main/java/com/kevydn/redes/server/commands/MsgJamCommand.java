package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

import java.util.Set;

public class MsgJamCommand extends MsgCommand {

    @Override
    public String name() { return "/jam"; }

    @Override
    public String description() { return "Manda uma mensagem para todos na mesma jam: /jam <mensagem>"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        String msg = validateMessage(cmd, ctx);
        if (msg == null) return;

        ClientHandler sender = (ClientHandler) ctx;
        String songName = sender.getJam();
        if (songName == null) {
            ctx.send("/msg Você não está em uma jam no momento.");
            return;
        }

        Set<String> jamClients = Server.getJamClients(songName);
        String formatted = formatMessage(sender, msg, "jam");

        // TODO reutilizar broadcast para dar match nesses valores
        for (String username : jamClients) {
            ClientHandler receiver = Server.getClient(username);
            if (receiver != null && receiver != sender) {
                receiver.send(formatted);
            }
        }
    }

    @Override
    public int expectedArgs() {
        return 1;
    }
}
