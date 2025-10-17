package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.ClientNetwork;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;

public class LoginOKCommand implements Command {

    @Override
    public String name() { return "/loginok"; }

    @Override
    public String description() { return "Login realizado com sucesso!"; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        String username = cmd.arg(0);
        ClientNetwork client = (ClientNetwork) ctx;
        client.setUsername(username);
    }

    @Override
    public int expectedArgs() { return 1; }
}
