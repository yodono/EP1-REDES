package com.kevydn.redes.client;

import com.kevydn.redes.client.commands.PlayCommand;
import com.kevydn.redes.protocol.CommandContext;
import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.ParsedCommand;

/**
 * Interpreta e executa comandos vindos do servidor.
 * Usa a mesma infraestrutura de protocolo do servidor.
 */
public class ClientCommandHandler extends CommandHandler {

    public ClientCommandHandler(CommandContext ctx) {
        super(ctx);
    }

    @Override
    protected Boolean precondition(ParsedCommand cmd) {
        return true;
    }

    @Override
    protected void registerCommands() {
        registry.register(new PlayCommand());
    }
}
