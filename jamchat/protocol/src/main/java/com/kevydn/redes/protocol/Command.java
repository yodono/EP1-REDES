package com.kevydn.redes.protocol;

// Trecho retirado e adaptado da conversa com ChatGPT: pág 99/172 do anexo ChatGPT Data Export - redes.pdf.
public interface Command {
    String name();
    String description();
    int expectedArgs();
    void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver);
}
