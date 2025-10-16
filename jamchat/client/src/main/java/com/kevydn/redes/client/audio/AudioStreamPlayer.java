package com.kevydn.redes.client.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AudioStreamPlayer implements Runnable {
    private final int CHUNK_SIZE = 4096; // tentativa de reduzir ruido aumentando o buffer (era 1024)

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(4445)) {
            System.out.println("UDP escutando na porta " + 4445);

            // Precisa parear com o MESMO AudioFormat do server
            // TODO receber esses valores por mensagem do servidor ao inves de hardcoded? da certo?
            AudioFormat format = new AudioFormat(
                    44100.0f, // taxa
                    16, // tamanho
                    2,        // canais (stereo)
                    true,     // com sinal
                    false     // little endian
            );

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(info);

            // tentativa de reduzir ruido aumentando o buffer de playback (SrouceDataLine)
            int playbackBufferSize = 4096 * 4; // Example: 16 KB buffer
            speaker.open(format, playbackBufferSize);
            speaker.open(format);
            speaker.start();

            byte[] buffer = new byte[CHUNK_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // comenta buffer compartilhado
                // byte[] audioData = packet.getData();
                // int length = packet.getLength();

                // tentativa de reduzir ruido, copiando apenas a parte do buffer que interessa
                int length = packet.getLength();
                byte[] audioData = new byte[length];
                System.arraycopy(packet.getData(), 0, audioData, 0, length);

                // Escreve no speaker (API Java para reproduzir som)
                // Usa o byte array "limpo" que foi copiado
                speaker.write(audioData, 0, length);
            }
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
