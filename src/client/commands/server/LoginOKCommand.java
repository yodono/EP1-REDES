package client.commands.server;

import client.Client;
import protocol.Command;
import protocol.CommandContext;
import protocol.ParsedCommand;

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
