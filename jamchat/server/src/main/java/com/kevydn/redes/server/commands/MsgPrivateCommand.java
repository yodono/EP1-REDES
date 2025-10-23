package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;
import com.kevydn.redes.server.Server;

public class MsgPrivateCommand extends MsgCommand {

    @Override
    public String name() { return "/to"; }

    @Override
    public String description() { return "Manda uma mensagem privada: /to <username> <mensagem>"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        if (cmd.args().size() < 2) {
            ctx.send("/msg Uso: /to <username> <mensagem>");
            return;
        }

        String targetUsername = cmd.arg(0);
        String msg = cmd.arg(1);

        ClientHandler sender = (ClientHandler) ctx;
        if (sender.getUsername().equals(targetUsername)) {
            sender.send("/msg Vá fazer amizades! Pare de falar com si mesmo!");
            return;
        }

        ClientHandler receiver = Server.getClient(targetUsername);
        if (targetUsername == null || receiver == null) {
            ctx.send("/msg Usuário '" + targetUsername + "' não encontrado ou offline.");
            return;
        }

        String formatted = formatMessage(sender, msg, targetUsername);
        receiver.send(formatted);
        sender.send(formatted);
    }

    @Override
    public int expectedArgs() {
        return 2;
    }
}
