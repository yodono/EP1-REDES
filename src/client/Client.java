package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente que se conecta ao servidor e troca mensagens em tempo real.
 */
public class Client {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

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
            Scanner scanner = new Scanner(System.in);

            // Thread para receber mensagens do servidor
            new Thread(new ServerListener(in)).start();

            // Loop principal de envio
            String serverMessage;
            String message;

            // Aguarda a primeira mensagem do servidor (solicitação de login)
            if ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                String username = scanner.nextLine();
                out.println(username); // envia o nome de usuário
            }

            // Agora o cliente já está logado
            System.out.println("Digite mensagens (ou 'sair' para encerrar):");
            while (true) {
                message = scanner.nextLine();
                if (message.equalsIgnoreCase("sair")) {
                    break;
                }
                out.println(message);
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

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
        client.start();
    }
}
