package com.kevydn.redes.server.audio;

import com.kevydn.redes.server.Server;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AudioStreamer implements Runnable {
    private final int CHUNK_SIZE = 4096; // tentativa de reduzir ruido aumentando o buffer (era 1024)
    private final String songName;
    private final int port;

    public AudioStreamer(String songName, int port) {
        this.songName = songName;
        this.port = port;
    }

    @Override
    public void run() {
        String filePath = "resources/audio/songs/" + songName;

        File file = new File( filePath);
        if (!file.exists()) {
            System.err.println("Arquivo não encontrado: " + filePath);
            return;
        }

        try (DatagramSocket socket = new DatagramSocket();
             InputStream rawAudioStream = new FileInputStream(file)
        ) {
            InetAddress clientAddress = InetAddress.getByName("localhost");

            // mark/reset erro com InputStream direto, usando o BufferedInputStream resolve
            BufferedInputStream bufferedStream = new BufferedInputStream(rawAudioStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
            AudioFormat format = audioInputStream.getFormat();
            
            System.out.println("Formato de audio: " + format);

            // Prepara buffer para ler AudioInputStream em chunks
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;

            while (!Server.getJamClients(songName).isEmpty() && (bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, clientAddress, port);
                socket.send(packet);

                System.out.println("Chunk enviado de tamanho: " + bytesRead);

                // Precisamos dar sleep na thread para não encher o buffer do cliente muito rápido
                // E parear a taxa de reprodução do áudio com a taxa de envio?? not sure
                int frameSize = format.getFrameSize(); // exemplo 16-bit mono
                float frameRate = format.getFrameRate(); // exemplo 44100
                long sleepTime = (long) (1000 * (bytesRead / (frameSize * frameRate)));
                Thread.sleep(Math.max(1, sleepTime));
            }

            System.out.println("Streaming de audio finalizado.");
        } catch (IOException | UnsupportedAudioFileException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            Server.removeJam(songName);
        }
    }
}
