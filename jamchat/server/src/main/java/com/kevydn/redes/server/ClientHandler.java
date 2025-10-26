package com.kevydn.redes.server;

import com.kevydn.redes.protocol.NetworkContext;
import com.kevydn.redes.protocol.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Manipula a comunicação com um único cliente.
 * Cada instância desta classe é executada em sua própria thread.
 * Trecho retirado e adaptado da conversa com ChatGPT: pág 97/172 do anexo ChatGPT Data Export - redes.pdf.
 */
public class ClientHandler implements Runnable, NetworkContext {

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String jam;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Inicializa os canais de entrada e saída do socket
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // 1. Inicializa tratador de comandos e pergunta por login
            send("/msg Bem-vindo ao chat! Por favor, digite seu nome de usuario:");
            CommandHandler handler = new ServerCommandHandler(this);

            // 2. Processamento de mensagens do cliente
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                handler.handle(clientMessage);
            }
        } catch (SocketException e) {
            System.out.println("Cliente " + (username != null ? username : "") + " desconectou abruptamente.");
        } catch (IOException e) {
            System.err.println("Erro no handler do cliente " + (username != null ? username : "") + ": " + e.getMessage());
        } finally {
            // 3. Lógica de Desconexão/Limpeza
            try {
                // Informa a todos que o usuário saiu
                if (username != null) {
                    Server.removeClient(username);
                }

                // Fecha os recursos
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
    }

    /**
     * Envia uma mensagem para este cliente específico.
     */
    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    public InetAddress getInetAddress() {
        return this.clientSocket.getInetAddress();
    }

    public void setJam(String songName) { this.jam = songName; }
    public String getJam() { return this.jam; }
}