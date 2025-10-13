package server.commands;

import protocol.Command;
import protocol.CommandContext;
import protocol.ParsedCommand;
import server.ClientHandler;
import server.Server;

public class MsgCommand implements Command {

    @Override
    public String name() { return "/all"; }

    @Override
    public String description() { return "Manda uma mensagem para todos: /all <mensagem>"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String msg = cmd.arg(0);
        if (msg == null || msg.isBlank()) {
            ctx.send("Uso: /all <mensagem>");
            return;
        }

        ClientHandler client = (ClientHandler) ctx;
        String broadcastMessage = "[" + client.getUsername() + "]: " + msg;
        Server.broadcastMessage(broadcastMessage, client);
    }
}
