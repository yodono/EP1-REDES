package server.commands;

import protocol.Command;
import protocol.CommandContext;
import protocol.ParsedCommand;
import server.ClientHandler;
import server.Server;

public class LoginCommand implements Command {

    @Override
    public String name() { return "/login"; }

    @Override
    public String description() { return "Faz login: /login <nome_usuario>"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String username = cmd.arg(0);
        if (username == null || username.isBlank()) {
            ctx.send("Uso: /login <nome_usuario>");
            return;
        }

        ClientHandler client = (ClientHandler) ctx;
        client.setUsername(username);
        Server.addClient(username, client);
        ctx.send("Login realizado com sucesso como " + username + "!");
    }
}
