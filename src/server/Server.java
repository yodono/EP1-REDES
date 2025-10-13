package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe principal do servidor de chat.
 * Responsável por aguardar conexões de clientes e criar uma nova thread
 * para cada cliente conectado.
 */
public class Server {
    // Porta em que o servidor ficará escutando.
    private static final int PORT = 12345;

    // Mapa para armazenar os manipuladores de cliente conectados, associando username ao handler.
    // Usamos ConcurrentHashMap para segurança em ambientes multithreaded.
    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de chat iniciando na porta " + PORT + "...");

            while (true) {
                // O método accept() bloqueia a execução até que um cliente se conecte.
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Cria um novo manipulador para o cliente e inicia em uma nova thread.
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adiciona um cliente à lista de clientes conectados.
     * Chamado pelo server.ClientHandler após o login bem-sucedido.
     * @param username O nome de usuário do cliente.
     * @param handler A instância do server.ClientHandler associada.
     */
    public static void addClient(String username, ClientHandler handler) {
        clients.put(username, handler);
    }

    /**
     * Remove um cliente da lista.
     * Chamado quando um cliente se desconecta.
     * @param username O nome de usuário a ser removido.
     */
    public static void removeClient(String username) {
        if (username != null) {
            clients.remove(username);
        }
    }

    /**
     * Encaminha uma mensagem para todos os clientes conectados, exceto para o remetente.
     * @param message A mensagem a ser transmitida.
     * @param senderHandler O handler do cliente que enviou a mensagem.
     */
    public static void broadcastMessage(String message, ClientHandler senderHandler) {
        // Itera sobre todos os client handlers conectados
        for (ClientHandler handler : clients.values()) {
            // Envia a mensagem apenas se não for o próprio remetente
            if (handler != senderHandler) {
                handler.sendMessage(message);
            }
        }
    }
}