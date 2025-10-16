package com.kevydn.redes.server.audio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class AudioStreamer implements Runnable {
    private final String filePath;

    public AudioStreamer(String filePath) throws IOException {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {

            // Cria uma mensagem maior para enviar
            String message =
                    """
                        Well, oh, they might wear classic Reeboks
                                Or knackered Converse
                                Or tracky bottoms tucked in socks
                                But all of that's what the point is not
                                The point's that there ain't no romance around there
                                And there's the truth that they can't see
                                They'd probably like to throw a punch at me
                                And if you could only see 'em, then you would agree
                                Agree that there ain't no romance around there
                                You know, oh, it's a funny thing you know
                                We'll tell 'em if you like
                                We'll tell 'em all tonight
                                They'll never listen
                                Because their minds are made up
                                And course it's all okay to carry on that way
                                'Cause over there, there's broken bones
                                There's only music, so that there's new ringtones
                                And it don't take no Sherlock Holmes
                                To see it's a little different around here
                                Don't get me wrong, though, there's boys in bands
                                And kids who like to scrap with pool cues in their hands
                                And just 'cause he's had a couple o' cans
                                He thinks it's all right to act like a dickhead
                                Don't you know, oh' it's a funny thing you know
                                We'll tell 'em if you like
                                We'll tell 'em all tonight
                                They'll never listen
                                Because their minds are made up
                                And course it's all okay to carry on that way
                                But I said no
                                Oh no
                                Well, you won't get me to go
                                Not anywhere, not anywhere
                                No, I won't go
                                Oh no no
                                Well, over there, there's friends of mine
                                What can I say? I've known 'em for a long long time
                                And, yeah, they might overstep the line
                                But you just cannot get angry in the same way
                                No, not in the same way
                                Said, not in the same way
                                Oh no, oh no no
                    """;
            // DatagramSocket envia byte[], precisa converter
            byte[] buffer = message.getBytes(StandardCharsets.UTF_8);

            int totalLength = buffer.length;
            int packetSize = 1024; // Tamanho de cada pacote
            int totalPackets = (int) Math.ceil((double) totalLength / packetSize);

            // Envia os pacotes com o número da sequência
            for (int i = 0; i < totalPackets; i++) {
                int start = i * packetSize;
                int end = Math.min(start + packetSize, totalLength);

                byte[] packetData = new byte[end - start];
                System.arraycopy(buffer, start, packetData, 0, end - start);

                // Prepara o pacote com dados e o número da sequência
                String header = "SEQ:" + i + "/" + totalPackets + " ";
                byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);

                // Junta os pacotes de header e mensagem em um
                byte[] finalPacket = new byte[headerBytes.length + packetData.length];
                System.arraycopy(headerBytes, 0, finalPacket, 0, headerBytes.length);
                System.arraycopy(packetData, 0, finalPacket, headerBytes.length, packetData.length);

                // Monta e envia o pacote final
                DatagramPacket packet = new DatagramPacket(finalPacket, finalPacket.length, InetAddress.getByName("localhost"), 4445);
                socket.send(packet);
                System.out.println(">>> Enviado pacote " + (i + 1) + " de " + totalPackets);
            }
        } catch (SocketException e) {
            System.out.println("Erro ao criar socket para música");
        } catch (UnknownHostException e) {
            System.out.println("Erro ao criar pacote para música");
        } catch (IOException e) {
            System.out.println("Erro ao enviar pacote");
        }
    }
}
