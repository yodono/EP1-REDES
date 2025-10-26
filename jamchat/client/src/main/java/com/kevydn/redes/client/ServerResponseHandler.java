package com.kevydn.redes.client;

import com.kevydn.redes.client.commands.JoinJamCommand;
import com.kevydn.redes.client.commands.LoginOKCommand;
import com.kevydn.redes.client.commands.LogoutOKCommand;
import com.kevydn.redes.client.commands.MsgCommand;
import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.ParsedCommand;

/**
 * Interpreta e executa comandos vindos do servidor.
 * Usa a mesma infraestrutura de protocolo do servidor.
 * Trecho retirado e adaptado da conversa com ChatGPT: pág 125/172 do anexo ChatGPT Data Export - redes.pdf.
 */
public class ServerResponseHandler extends CommandHandler {

    public ServerResponseHandler(NetworkContext ctx, MessageObserver listener) {
        super(ctx, listener);
    }

    @Override
    protected Boolean precondition(ParsedCommand cmd) {
        return true;
    }

    @Override
    protected void registerCommands() {
        registry.register(new LoginOKCommand());
        registry.register(new LogoutOKCommand());
        registry.register(new MsgCommand());
        registry.register(new JoinJamCommand());
    }

    @Override
    protected void handleUnknown() {}
}
