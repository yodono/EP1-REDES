package com.kevydn.redes.client.audio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class AudioStreamPlayer implements Runnable {

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket(4445)) {
            System.out.println("UDP escutando na porta " + 4445);

            byte[] receiveData = new byte[1024]; // Buffer para dados recebidos
            Map<Integer, StringBuilder> packetMap = new HashMap<>(); // Mapeia os pacotes pela sequência

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket); // Bloqueia até um pacote ser recebido

                // Extrai dados do pacote recebido
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Extrai número da sequência e total de pacotes
                int seqStart = receivedMessage.indexOf("SEQ:") + 4;
                int seqEnd = receivedMessage.indexOf(" ", seqStart);
                String seqInfo = receivedMessage.substring(seqStart, seqEnd);
                String[] seqParts = seqInfo.split("/");
                int currentSeq = Integer.parseInt(seqParts[0]);
                int totalPackets = Integer.parseInt(seqParts[1]);

                // Obtém o conteúdo da mensagem (sem o cabeçalho)
                String content = receivedMessage.substring(seqEnd + 1);

                // Adiciona o conteúdo ao map de pacotes
                packetMap.putIfAbsent(currentSeq, new StringBuilder());
                packetMap.get(currentSeq).append(content);

                // Verifica se todos os pacotes foram recebidos
                if (packetMap.size() == totalPackets) {
                    StringBuilder fullMessage = new StringBuilder();
                    for (int i = 0; i < totalPackets; i++) {
                        fullMessage.append(packetMap.get(i));
                    }

                    System.out.println("Mensagem reconstruída: " + fullMessage);
                    packetMap.clear(); // Limpa os pacotes recebidos para próxima transmissão
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
