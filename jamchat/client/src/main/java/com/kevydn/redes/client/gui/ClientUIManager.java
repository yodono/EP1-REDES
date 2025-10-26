package com.kevydn.redes.client.gui;

import com.kevydn.redes.client.ClientCommandHandler;
import com.kevydn.redes.client.ClientNetwork;
import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.MessageObserver;

import javax.swing.SwingUtilities;

/**
 * Controla a interface gráfica do cliente.
 * Trecho retirado e adaptado da conversa com ChatGPT: pág 55/172 do anexo ChatGPT Data Export - redes.pdf.
 */
public class ClientUIManager implements MessageObserver {
    private final ClientNetwork network;
    private BasicWindow window;
    private CommandHandler clientCommandHandler;

    public ClientUIManager(ClientNetwork network) {
        this.network = network;
        this.clientCommandHandler = new ClientCommandHandler(network);
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
        if (!message.startsWith("/")) message = "/all " + message;
        clientCommandHandler.handle(message);
        network.send(message);
    }

    /** Chamado quando uma nova mensagem chega do servidor */
    public void displayMessage(String message) {
        if (window != null) window.appendMessage(message);
    }
}
