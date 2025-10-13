package server;

import protocol.CommandHandler;
import protocol.ParsedCommand;
import server.commands.HelpCommand;
import server.commands.LoginCommand;
import server.commands.MsgCommand;

public class ServerCommandHandler extends CommandHandler {

    public ServerCommandHandler(ClientHandler client) {
        super(client);
    }

    @Override
    protected void registerCommands() {
        registry.register(new LoginCommand());
        registry.register(new MsgCommand());
        registry.register(new HelpCommand(registry));
    }

    @Override
    protected Boolean precondition(ParsedCommand cmd) {
        if (ctx.getUsername() == null && !cmd.name().equals("/login")) {
            ctx.send(">>> Você precisa fazer login antes de usar outros comandos.");
            return false;
        }
        return true;
    }
}
