package com.kevydn.redes.client;

import com.kevydn.redes.client.gui.ClientUIManager;

// Orquestrador do cliente
// Trecho retirado e adaptado da conversa com ChatGPT: pág 65/172 do anexo ChatGPT Data Export - redes.pdf.
public class ClientApp {
    public static void main(String[] args) {
        String serverIp = args[0];
        int serverPort = Integer.parseInt(args[1]);
        boolean guiMode = args.length == 2 || !args[2].equals("--headless");

        ClientNetwork network = new ClientNetwork(serverIp, serverPort);

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
