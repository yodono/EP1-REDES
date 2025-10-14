package client;

import protocol.CommandContext;
import protocol.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente que se conecta ao servidor e troca mensagens em tempo real.
 */
public class Client implements CommandContext {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            System.out.println("Conectado ao servidor em " + host + ":" + port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread para receber mensagens do servidor
            CommandHandler serverHandler = new ServerResponseHandler(this);
            new Thread(new ServerListener(in, serverHandler)).start();

            Scanner scanner = new Scanner(System.in);
            while (true) { // Loop principal de envio de mensagens
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("/sair")) break;
                send(message);
            }

            close();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    private void close() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
            System.out.println("Desconectado do servidor.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void out(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
        client.start();
    }
}
