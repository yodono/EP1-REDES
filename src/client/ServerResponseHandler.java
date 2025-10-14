package client;

import client.commands.server.LoginOKCommand;
import client.commands.server.MsgCommand;
import protocol.CommandContext;
import protocol.CommandHandler;
import protocol.ParsedCommand;

/**
 * Interpreta e executa comandos vindos do servidor.
 * Usa a mesma infraestrutura de protocolo do servidor.
 */
public class ServerResponseHandler extends CommandHandler {

    public ServerResponseHandler(CommandContext ctx) {
        super(ctx);
    }

    @Override
    protected Boolean precondition(ParsedCommand cmd) {
        return true;
    }

    @Override
    protected void registerCommands() {
        registry.register(new LoginOKCommand());
        registry.register(new MsgCommand());
    }
}
