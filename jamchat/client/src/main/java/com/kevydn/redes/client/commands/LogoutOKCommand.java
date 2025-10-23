package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.ClientNetwork;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.ParsedCommand;

public class LogoutOKCommand implements Command {

    @Override
    public String name() { return "/logoutok"; }

    @Override
    public String description() { return "Indica que logout foi realizado com sucesso."; }

    @Override
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        ClientNetwork client = (ClientNetwork) ctx;
        client.setUsername(null);
        client.close();
    }

    @Override
    public int expectedArgs() { return 1; }
}
