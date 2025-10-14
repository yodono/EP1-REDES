package com.kevydn.redes.server;

import com.kevydn.redes.protocol.CommandContext;
import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.ParsedCommand;
import com.kevydn.redes.server.commands.HelpCommand;
import com.kevydn.redes.server.commands.LoginCommand;
import com.kevydn.redes.server.commands.MsgCommand;

public class ServerCommandHandler extends CommandHandler {

    public ServerCommandHandler(CommandContext ctx) {
        super(ctx);
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
            ctx.send("/msg Você precisa fazer login antes de usar outros comandos.");
            return false;
        }
        return true;
    }
}
