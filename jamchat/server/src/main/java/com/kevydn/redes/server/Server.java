package com.kevydn.redes.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe principal do servidor de chat.
 * Responsável por aguardar conexões de clientes e criar uma nova thread
 * para cada cliente conectado.
 */
public class Server {

    // porta de jam padrão para streaming UDP
    // pode ser sobrescrita por args
    private static int defaultJamPort = 4000;

    // (username, handler)
    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    // (songName, port)
    private static final Map<String, Integer> jams = new ConcurrentHashMap<>();
    // (songName, clients)
    private static final Map<String, Set<String>> jamClients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        if (args.length == 2 && args[1] != null) defaultJamPort = Integer.parseInt(args[1]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor de chat iniciando na porta " + port + "...");

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
        if (username == null) return;

        ClientHandler clientHandler = clients.get(username);
        String jam = clientHandler.getJam();

        clients.remove(username);
        String logoutMessage = username + " saiu do chat.";
        System.out.println(">>> " + logoutMessage);
        Server.broadcastMessage("/msg " + logoutMessage, null);

        if (jam == null) return;
        Server.removeClientFromJam(jam, username);
    }

    public static ClientHandler getClient(String username) {
        return clients.get(username);
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
                handler.send(message);
            }
        }
    }

    /**
     * Adiciona uma música ao map de jams atualmente em progresso.
     * @param songName O nome da música a ser tocada.
     */
    public static void addJam(String songName) {
        jams.put(songName, createJamPort());
    }

    public static void removeJam(String songName) {
        jams.remove(songName);
    }

    public static boolean hasJam(String songName) {
        return jams.containsKey(songName);
    }

    public static int getJamPort(String songName) {
        return jams.get(songName);
    }

    private static int createJamPort() {
        // TODO isso aqui basta? talvez ter um comando para ver se a porta esta em uso e pegar um outro valor
        // Ler default UDP port de args
        if (jams.isEmpty()) return defaultJamPort;

        // Vai criar portas novas para cada streaming de jam, 4001, 4002, 4003, etc...
        return jams.values().stream().sorted().toList().get(jams.size() - 1) + 1;
    }

    public static void connectClientToJam(String songName, String username) {
        if (jamClients.containsKey(songName)) {
            jamClients.get(songName).add(username);
            return;
        }

        // manter lista thread-safe
        Set<String> connectedClients = ConcurrentHashMap.newKeySet();
        connectedClients.add(username);
        jamClients.put(songName, connectedClients);
    }

    public static void removeClientFromJam(String songName, String username) {
        jamClients.get(songName).remove(username);
    }

    public static Set<String> getJamClients(String songName) {
        if (songName == null || jamClients.isEmpty() || jamClients.get(songName) == null) return ConcurrentHashMap.newKeySet();
        return jamClients.get(songName);
    }
}