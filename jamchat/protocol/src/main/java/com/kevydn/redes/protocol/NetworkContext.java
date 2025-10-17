package com.kevydn.redes.protocol;

/**
 * Interface que abstrai ambiente de network
 */
public interface NetworkContext {
    void send(String message);
    String getUsername();
}
