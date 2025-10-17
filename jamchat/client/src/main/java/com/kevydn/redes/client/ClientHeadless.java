package com.kevydn.redes.client;

import com.kevydn.redes.protocol.CommandHandler;
import com.kevydn.redes.protocol.MessageObserver;

import java.util.Scanner;

// Modo terminal (sem interface gráfica).
public class ClientHeadless implements MessageObserver {
    private final ClientNetwork network;

    public ClientHeadless(ClientNetwork network) {
        this.network = network;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        CommandHandler clientHandler = new ClientCommandHandler(network);

        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("/sair")) break;
            clientHandler.handle(message);
            network.send(message);
        }

        network.close();
    }

    @Override
    public void onMessageReceived(String message) {
        System.out.println(message);
    }
}
