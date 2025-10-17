package com.kevydn.redes.client;

import com.kevydn.redes.protocol.MessageObserver;
import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Camada de rede do cliente: conecta, envia e recebe mensagens.
public class ClientNetwork implements NetworkContext {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private CommandHandler serverHandler;
    private String username;

    public ClientNetwork(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(CommandHandler serverHandler) throws IOException {
        this.serverHandler = serverHandler;
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Thread para ouvir o servidor sem bloquear o cliente
        new Thread(new ServerListener(in, serverHandler)).start();
        System.out.println("Conectado ao servidor em " + host + ":" + port);
    }

    public void send(String message) {
        if (out != null) out.println(message);
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
}
