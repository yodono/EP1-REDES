package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.ClientHandler;

public abstract class MsgCommand implements Command {

    protected String formatMessage(ClientHandler sender, String msg, String scope) {
        StringBuilder sb = new StringBuilder("/msg ");

        switch (scope.toLowerCase()) {
            case "all" -> sb.append("[ALL]");
            case "jam" -> sb.append("[JAM]");
            default -> sb.append("[").append(scope).append("]");
        }

        sb.append(" ").append(sender.getUsername()).append(": ").append(msg);
        return sb.toString();
    }


    protected String validateMessage(ParsedCommand cmd, NetworkContext ctx) {
        String msg = cmd.arg(0);
        if (msg == null || msg.isBlank()) {
            ctx.send("/msg Uso incorreto do comando.");
            return null;
        }
        return msg;
    }

    public abstract int expectedArgs();

    public abstract void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver);
}
