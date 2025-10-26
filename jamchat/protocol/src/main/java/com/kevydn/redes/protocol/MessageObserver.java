package com.kevydn.redes.protocol;

/**
 * Interface que deverá implementar tratamento para mensagens trocadas pelo protocolo
 */
public interface MessageObserver {
    void onMessageReceived(String message);
}
