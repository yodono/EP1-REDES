package client;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerListener implements Runnable {
    private final BufferedReader in;

    public ServerListener(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            System.out.println("Conexão encerrada pelo servidor.");
        }
    }
}
