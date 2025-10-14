package com.kevydn.redes.client.audio;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer {

    private AdvancedPlayer player;
    private Thread playThread;
    private volatile boolean playing = false;
    private volatile boolean paused = false;

    public void play(String filePath) {
        stop();

        playThread = new Thread(() -> {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
                player = new AdvancedPlayer(bis);
                playing = true;
                paused = false;
                System.out.println("▶ Tocando: " + filePath);

                player.play(); // blocking until finished or stopped

            } catch (JavaLayerException | IOException e) {
                System.err.println("Erro ao tocar MP3: " + e.getMessage());
            } finally {
                playing = false;
            }
        });

        playThread.start();
    }

    public synchronized void pause() {
        // JLayer does not support native pause/resume —
        // you'd need to track frame position and restart from there.
        paused = true;
        System.out.println("⏸ Pause não é suportado nativamente no JLayer sem controle de frames.");
    }

    public void stop() {
        playing = false;
        if (player != null) {
            player.close();
            System.out.println("⏹ Reprodução parada.");
        }
    }

    public boolean isPlaying() {
        return playing;
    }
}
