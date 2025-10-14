package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.Client;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.CommandContext;
import com.kevydn.redes.protocol.ParsedCommand;

public class LoginOKCommand implements Command {

    @Override
    public String name() { return "/loginok"; }

    @Override
    public String description() { return "Login realizado com sucesso!"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String username = cmd.arg(0);
        Client client = (Client) ctx;
        client.setUsername(username);
    }

    @Override
    public int expectedArgs() { return 1; }
}
