package com.kevydn.redes.client;

import com.kevydn.redes.client.gui.ClientUIManager;

// Orquestrador do cliente
public class ClientApp {
    public static void main(String[] args) {
        boolean guiMode = args.length == 0 || !args[0].equals("--headless");

        ClientNetwork network = new ClientNetwork("localhost", 12345);

        try {

            if (guiMode) {
                ClientUIManager ui = new ClientUIManager(network);
                ServerResponseHandler serverHandler = new ServerResponseHandler(network, ui);
                network.connect(serverHandler);

                ui.startUI();
            } else {
                ClientHeadless headless = new ClientHeadless(network);
                ServerResponseHandler serverHandler = new ServerResponseHandler(network, headless);
                network.connect(serverHandler);

                headless.start();
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar cliente: " + e.getMessage());
        }
    }
}
