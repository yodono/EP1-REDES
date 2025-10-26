package com.kevydn.redes.client;

import com.kevydn.redes.protocol.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;

// Trecho retirado e adaptado da conversa com ChatGPT: pág 132/172 do anexo ChatGPT Data Export - redes.pdf.
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
