package com.kevydn.redes.client;

import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.ParsedCommand;

/**
 * TODO sem uso por enquanto, ja que mudamos a logica de play. Remover?
 * Interpreta e executa comandos do cliente, para o cliente (comandos locais).
 * Usa a mesma infraestrutura de protocolo do servidor.
 */
public class ClientCommandHandler extends CommandHandler {

    public ClientCommandHandler(NetworkContext ctx) {
        super(ctx);
    }

    @Override
    protected Boolean precondition(ParsedCommand cmd) {
        return true;
    }

    @Override
    protected void registerCommands() {}

    @Override
    protected void handleUnknown() {}
}
