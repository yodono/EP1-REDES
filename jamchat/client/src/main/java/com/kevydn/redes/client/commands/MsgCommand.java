package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.ClientNetwork;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;

public class MsgCommand implements Command {

    @Override
    public String name() { return "/msg"; }

    @Override
    public String description() { return "Recebe uma mensagem do servidor"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver observer) {
        String msg = cmd.arg(0);
        assert msg != null;
        msg = msg.replace("\\n", "\n");

        if (observer != null) {
            observer.onMessageReceived(msg);
        }
    }

    @Override
    public int expectedArgs() { return 1; }
}
