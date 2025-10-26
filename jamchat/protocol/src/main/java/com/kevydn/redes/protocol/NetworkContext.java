package com.kevydn.redes.protocol;

/**
 * Interface que abstrai ambiente de network
 * Trecho retirado e adaptado da conversa com ChatGPT: pág 109/172 do anexo ChatGPT Data Export - redes.pdf.
 */
public interface NetworkContext {
    void send(String message);
    String getUsername();
}
