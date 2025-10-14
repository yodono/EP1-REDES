package client.commands.server;

import client.Client;
import protocol.Command;
import protocol.CommandContext;
import protocol.ParsedCommand;
import server.ClientHandler;
import server.Server;

public class MsgCommand implements Command {

    @Override
    public String name() { return "/msg"; }

    @Override
    public String description() { return "Recebe uma mensagem do servidor"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String msg = cmd.arg(0);
        assert msg != null;
        msg = msg.replace("\\n", "\n");
        Client client = (Client) ctx;
        client.out(msg);
    }

    @Override
    public int expectedArgs() { return 1; }
}
