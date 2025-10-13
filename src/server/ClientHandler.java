    package server;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.net.Socket;
    import java.net.SocketException;

    /**
     * Manipula a comunicação com um único cliente.
     * Cada instância desta classe é executada em sua própria thread.
     */
    public class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Inicializa os canais de entrada e saída do socket
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // 1. Lógica de Login
                out.println(">>> Bem-vindo ao chat! Por favor, digite seu nome de usuario:");
                this.username = in.readLine();

                // Adiciona o cliente ao mapa do servidor
                Server.addClient(this.username, this);
                System.out.println(this.username + " logou.");

                // Informa aos outros usuários que um novo cliente entrou
                String loginMessage = ">>> " + this.username + " entrou no chat.";
                Server.broadcastMessage(loginMessage, this);
                out.println(">>> Olá, " + this.username + "! Você está conectado.");

                // 2. Loop principal de leitura de mensagens
                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    String broadcastMessage = "[" + this.username + "]: " + clientMessage;
                    System.out.println("Mensagem recebida de " + this.username + ": " + clientMessage);
                    Server.broadcastMessage(broadcastMessage, this);
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
                        String logoutMessage = ">>> " + username + " saiu do chat.";
                        System.out.println(logoutMessage);
                        Server.broadcastMessage(logoutMessage, this);
                    }

                    // Fecha os recursos
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (clientSocket != null) clientSocket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }

        /**
         * Envia uma mensagem para este cliente específico.
         * @param message A mensagem a ser enviada.
         */
        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }