package com.kevydn.redes.client.gui;

import com.kevydn.redes.client.ClientNetwork;
import com.kevydn.redes.protocol.MessageObserver;

import javax.swing.SwingUtilities;

/**
 * Controla a interface gráfica do cliente.
 */
public class ClientUIManager implements MessageObserver {
    private final ClientNetwork network;
    private BasicWindow window;

    public ClientUIManager(ClientNetwork network) {
        this.network = network;
    }

    public void startUI() {
        SwingUtilities.invokeLater(() -> {
            window = new BasicWindow(this);
            window.show();
        });
    }

    @Override
    public void onMessageReceived(String message) {
        displayMessage(message);
    }

    /** Chamado pela janela quando o usuário envia uma mensagem */
    public void onUserMessage(String message) {
        network.send(message);
    }

    /** Chamado quando uma nova mensagem chega do servidor */
    public void displayMessage(String message) {
        if (window != null) window.appendMessage(message);
    }
}
