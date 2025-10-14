package client;

import protocol.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerListener implements Runnable {
    private final BufferedReader in;
    private final CommandHandler handler;

    public ServerListener(BufferedReader in, CommandHandler handler) {
        this.in = in;
        this.handler = handler;
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                handler.handle(msg); // isso pode modificar estado do Client via contexto do CommandHandler
            }
        } catch (IOException e) {
            System.out.println("Conexão encerrada pelo servidor.");
        }
    }
}
