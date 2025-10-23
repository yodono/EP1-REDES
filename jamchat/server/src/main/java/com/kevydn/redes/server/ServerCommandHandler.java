package com.kevydn.redes.server;

import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.commands.*;

public class ServerCommandHandler extends CommandHandler {

    public ServerCommandHandler(NetworkContext ctx) {
        super(ctx);
    }

    @Override
    protected void registerCommands() {
        registry.register(new LoginCommand());
        registry.register(new MsgAllCommand());
        registry.register(new MsgJamCommand());
        registry.register(new MsgPrivateCommand());
        registry.register(new PlayCommand());
        registry.register(new ListJamsCommand());
        registry.register(new HelpCommand(registry));
    }

    @Override
    protected Boolean precondition(ParsedCommand cmd) {
        if (networkContext.getUsername() == null && !cmd.name().equals("/login")) {
            networkContext.send("/msg Você precisa fazer login antes de usar outros comandos.");
            return false;
        }
        return true;
    }

    @Override
    protected void handleUnknown() {
        networkContext.send("/msg Comando desconhecido. Use /help para ver a lista.");
    }
}
